package com.teasmile.exception;

/**
 * 未登录或登录态已失效异常，通常由 JWT 拦截器在令牌缺失/非法时抛出
 */
public class UserNotLoginException extends BusinessException {

    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String message) {
        super(message);
    }
}
