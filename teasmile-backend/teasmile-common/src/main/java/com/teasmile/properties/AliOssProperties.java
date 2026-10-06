package com.teasmile.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 对象存储参数，配置前缀 {@code teasmile.alioss}，用于商品图片上传。
 */
@Component
@ConfigurationProperties(prefix = "teasmile.alioss")
@Data
public class AliOssProperties {

    /** 访问域名节点 */
    private String endpoint;

    /** 访问密钥 Id */
    private String accessKeyId;

    /** 访问密钥密文 */
    private String accessKeySecret;

    /** 存储空间名称 */
    private String bucketName;
}
