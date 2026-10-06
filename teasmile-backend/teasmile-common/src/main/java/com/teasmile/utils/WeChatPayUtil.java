package com.teasmile.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.teasmile.properties.WeChatProperties;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * 微信支付 V3 工具：封装小程序 JSAPI 下单（含调起支付二次签名）与申请退款。
 * 签名、验签及证书更新由官方 {@link WechatPayHttpClientBuilder} 构建的客户端自动完成。
 */
@Component
public class WeChatPayUtil {

    /** JSAPI 统一下单地址 */
    private static final String JSAPI_ORDER_URL = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    /** 申请退款地址 */
    private static final String REFUND_URL = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    /** 元转分的倍数 */
    private static final BigDecimal YUAN_TO_CENT = new BigDecimal(100);

    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 加载商户私钥与平台证书，构造具备自动签名验签能力的 HTTP 客户端。
     */
    private CloseableHttpClient buildHttpClient() throws FileNotFoundException {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                new FileInputStream(new File(weChatProperties.getPrivateKeyFilePath())));
        X509Certificate platformCert = PemUtil.loadCertificate(
                new FileInputStream(new File(weChatProperties.getWeChatPayCertFilePath())));
        List<X509Certificate> platformCerts = Collections.singletonList(platformCert);

        return WechatPayHttpClientBuilder.create()
                .withMerchant(weChatProperties.getMchid(),
                        weChatProperties.getMchSerialNo(), merchantPrivateKey)
                .withWechatPay(platformCerts)
                .build();
    }

    private String doPost(String url, String body) throws Exception {
        try (CloseableHttpClient httpClient = buildHttpClient()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            httpPost.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());
            httpPost.setEntity(new StringEntity(body, "UTF-8"));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    private String doGet(String url) throws Exception {
        try (CloseableHttpClient httpClient = buildHttpClient()) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
            httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
            httpGet.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    /**
     * 调用统一下单接口获取预支付交易会话标识。
     */
    private String createJsapiOrder(String orderNo, BigDecimal amountYuan,
                                    String description, String openid) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("appid", weChatProperties.getAppid());
        requestBody.put("mchid", weChatProperties.getMchid());
        requestBody.put("description", description);
        requestBody.put("out_trade_no", orderNo);
        requestBody.put("notify_url", weChatProperties.getNotifyUrl());

        JSONObject amount = new JSONObject();
        amount.put("total", yuanToCent(amountYuan));
        amount.put("currency", "CNY");
        requestBody.put("amount", amount);

        JSONObject payer = new JSONObject();
        payer.put("openid", openid);
        requestBody.put("payer", payer);

        return doPost(JSAPI_ORDER_URL, requestBody.toJSONString());
    }

    /**
     * 小程序支付：统一下单后按微信规则二次签名，返回小程序端 wx.requestPayment 所需参数。
     *
     * @param orderNo     商户订单号
     * @param totalAmount 订单金额（单位：元）
     * @param description 商品描述
     * @param openid      支付人 openid
     */
    public JSONObject pay(String orderNo, BigDecimal totalAmount,
                          String description, String openid) throws Exception {
        String orderResponse = createJsapiOrder(orderNo, totalAmount, description, openid);
        JSONObject result = JSON.parseObject(orderResponse);

        String prepayId = result.getString("prepay_id");
        if (prepayId == null) {
            // 下单失败时原样返回微信的错误信息，便于排查
            return result;
        }

        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomNumeric(32);
        String packageValue = "prepay_id=" + prepayId;

        // 调起支付签名串：appId\ntimeStamp\nnonceStr\npackage\n
        String signContent = String.join("\n",
                weChatProperties.getAppid(), timeStamp, nonceStr, packageValue) + "\n";

        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(PemUtil.loadPrivateKey(
                new FileInputStream(new File(weChatProperties.getPrivateKeyFilePath()))));
        signer.update(signContent.getBytes());
        String paySign = Base64.getEncoder().encodeToString(signer.sign());

        JSONObject paymentParams = new JSONObject();
        paymentParams.put("timeStamp", timeStamp);
        paymentParams.put("nonceStr", nonceStr);
        paymentParams.put("package", packageValue);
        paymentParams.put("signType", "RSA");
        paymentParams.put("paySign", paySign);
        return paymentParams;
    }

    /**
     * 申请退款
     *
     * @param outTradeNo  原商户订单号
     * @param outRefundNo 商户退款单号
     * @param refundAmount 退款金额（单位：元）
     * @param totalAmount  原订单总金额（单位：元）
     */
    public String refund(String outTradeNo, String outRefundNo,
                         BigDecimal refundAmount, BigDecimal totalAmount) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("out_trade_no", outTradeNo);
        requestBody.put("out_refund_no", outRefundNo);

        JSONObject amount = new JSONObject();
        amount.put("refund", yuanToCent(refundAmount));
        amount.put("total", yuanToCent(totalAmount));
        amount.put("currency", "CNY");
        requestBody.put("amount", amount);
        requestBody.put("notify_url", weChatProperties.getRefundNotifyUrl());

        return doPost(REFUND_URL, requestBody.toJSONString());
    }

    /**
     * 微信金额单位为分，且必须为整数
     */
    private int yuanToCent(BigDecimal yuan) {
        return yuan.multiply(YUAN_TO_CENT).setScale(2, RoundingMode.HALF_UP).intValue();
    }
}
