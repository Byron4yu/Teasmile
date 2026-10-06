package com.teasmile.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序登录与微信支付 V3 参数，配置前缀 {@code teasmile.wechat}。
 */
@Component
@ConfigurationProperties(prefix = "teasmile.wechat")
@Data
public class WeChatProperties {

    /** 小程序 appid */
    private String appid;

    /** 小程序密钥 */
    private String secret;

    /** 微信支付商户号 */
    private String mchid;

    /** 商户 API 证书序列号 */
    private String mchSerialNo;

    /** 商户 API 私钥文件路径（apiclient_key.pem） */
    private String privateKeyFilePath;

    /** APIv3 证书解密密钥 */
    private String apiV3Key;

    /** 微信支付平台证书文件路径 */
    private String weChatPayCertFilePath;

    /** 支付结果异步通知地址 */
    private String notifyUrl;

    /** 退款结果异步通知地址 */
    private String refundNotifyUrl;
}
