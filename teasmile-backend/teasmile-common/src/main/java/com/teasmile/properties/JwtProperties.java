package com.teasmile.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 令牌参数，管理端与用户端各自独立，配置前缀 {@code teasmile.jwt}。
 */
@Component
@ConfigurationProperties(prefix = "teasmile.jwt")
@Data
public class JwtProperties {

    /** 管理端：签名密钥 */
    private String adminSecretKey;
    /** 管理端：令牌有效期（毫秒） */
    private long adminTtl;
    /** 管理端：请求头 / 请求参数中携带令牌的名称 */
    private String adminTokenName;

    /** 用户端：签名密钥 */
    private String userSecretKey;
    /** 用户端：令牌有效期（毫秒） */
    private long userTtl;
    /** 用户端：请求头 / 请求参数中携带令牌的名称 */
    private String userTokenName;
}
