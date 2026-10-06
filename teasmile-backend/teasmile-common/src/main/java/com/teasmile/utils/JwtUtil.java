package com.teasmile.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 令牌工具：负责令牌的签发与解析验签，统一采用 HS256 对称签名。
 * <p>
 * 密钥只允许保存在服务端配置中，切勿下发给客户端。
 */
public final class JwtUtil {

    /** 签名算法 */
    private static final SignatureAlgorithm SIGN_ALGORITHM = SignatureAlgorithm.HS256;

    private JwtUtil() {
    }

    /**
     * 签发令牌
     *
     * @param secretKey 签名密钥
     * @param ttlMillis 有效期（毫秒）
     * @param claims    自定义载荷内容
     * @return 紧凑格式的 JWT 字符串
     */
    public static String createToken(String secretKey, long ttlMillis, Map<String, Object> claims) {
        long expireAt = System.currentTimeMillis() + ttlMillis;

        return Jwts.builder()
                // 自定义声明需先于标准声明设置，避免被覆盖
                .setClaims(claims)
                .signWith(SIGN_ALGORITHM, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(new Date(expireAt))
                .compact();
    }

    /**
     * 解析并校验令牌
     *
     * @param secretKey 签发时使用的同一把签名密钥
     * @param token     待解析令牌
     * @return 令牌载荷；签名错误或已过期时会抛出异常
     */
    public static Claims parseToken(String secretKey, String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
