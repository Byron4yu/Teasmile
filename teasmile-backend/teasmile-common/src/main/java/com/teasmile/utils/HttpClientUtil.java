package com.teasmile.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于 Apache HttpClient 封装的轻量 HTTP 客户端，
 * 主要用于调用微信平台接口（如登录凭证校验）。
 */
public final class HttpClientUtil {

    /** 连接、获取连接、读取数据的统一超时时间（毫秒） */
    private static final int TIMEOUT_MILLIS = 5000;

    private HttpClientUtil() {
    }

    /**
     * 发起 GET 请求，参数以 query string 形式拼接。
     */
    public static String doGet(String url, Map<String, String> params) {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String responseBody = "";
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                params.forEach(uriBuilder::addParameter);
            }
            URI uri = uriBuilder.build();

            HttpGet httpGet = new HttpGet(uri);
            response = client.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(response, client);
        }
        return responseBody;
    }

    /**
     * 发起 POST 请求，请求体为表单格式（application/x-www-form-urlencoded）。
     */
    public static String doPost(String url, Map<String, String> formParams) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig());

            if (formParams != null) {
                List<NameValuePair> pairs = new ArrayList<>(formParams.size());
                formParams.forEach((key, value) -> pairs.add(new BasicNameValuePair(key, value)));
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            }

            response = client.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } finally {
            closeQuietly(response, client);
        }
    }

    /**
     * 发起 POST 请求，请求体为 JSON 格式（application/json）。
     */
    public static String doPost4Json(String url, Map<String, String> jsonParams) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(buildRequestConfig());

            if (jsonParams != null) {
                JSONObject payload = new JSONObject();
                payload.putAll(jsonParams);
                StringEntity entity = new StringEntity(payload.toJSONString(), StandardCharsets.UTF_8);
                entity.setContentEncoding(StandardCharsets.UTF_8.name());
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }

            response = client.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } finally {
            closeQuietly(response, client);
        }
    }

    private static RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT_MILLIS)
                .setConnectionRequestTimeout(TIMEOUT_MILLIS)
                .setSocketTimeout(TIMEOUT_MILLIS)
                .build();
    }

    private static void closeQuietly(CloseableHttpResponse response, CloseableHttpClient client) {
        try {
            if (response != null) {
                response.close();
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
