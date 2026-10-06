package com.teasmile.exception;

/**
 * 登录密码校验失败异常
 */
public class PasswordErrorException extends BusinessException {

    public PasswordErrorException() {
        super();
    }

    public PasswordErrorException(String message) {
        super(message);
    }
}
