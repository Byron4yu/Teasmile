package com.teasmile.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * 阿里云 OSS 文件上传客户端。
 * <p>
 * 由配置类读取 {@code teasmile.alioss} 参数后构造为单例 Bean 使用。
 */
@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 将字节流上传到指定对象路径。
     *
     * @param bytes      文件内容
     * @param objectName 对象在 bucket 中的唯一键名
     * @return 文件可公开访问的 URL
     */
    public String upload(byte[] bytes, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            // 请求已到达 OSS，但被服务端拒绝
            log.error("OSS 服务端拒绝上传, errorCode={}, requestId={}, message={}",
                    oe.getErrorCode(), oe.getRequestId(), oe.getErrorMessage());
        } catch (ClientException ce) {
            // 客户端侧问题，多为网络不通
            log.error("OSS 客户端上传异常: {}", ce.getMessage());
        } finally {
            ossClient.shutdown();
        }

        // 访问路径拼装规则：https://bucket.endpoint/objectName
        String accessUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
        log.info("文件已上传至 OSS：{}", accessUrl);
        return accessUrl;
    }
}
