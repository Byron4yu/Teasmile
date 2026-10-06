package com.teasmile.exception;

/**
 * 登录失败异常（如微信授权码换取不到 openid 等场景）
 */
public class LoginFailedException extends BusinessException {

    public LoginFailedException(String message) {
        super(message);
    }
}
