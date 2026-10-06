package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * C 端会员微信登录成功返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 会员 id */
    private Long id;

    /** 微信 openid */
    private String openid;

    /** JWT 访问令牌 */
    private String token;
}
