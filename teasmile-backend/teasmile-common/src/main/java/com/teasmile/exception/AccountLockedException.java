package com.teasmile.exception;

/**
 * 账号被禁用（锁定）异常
 */
public class AccountLockedException extends BusinessException {

    public AccountLockedException() {
        super();
    }

    public AccountLockedException(String message) {
        super(message);
    }
}
