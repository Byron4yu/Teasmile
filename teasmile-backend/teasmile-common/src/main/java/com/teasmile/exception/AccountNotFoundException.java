package com.teasmile.exception;

/**
 * 登录账号不存在异常
 */
public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
