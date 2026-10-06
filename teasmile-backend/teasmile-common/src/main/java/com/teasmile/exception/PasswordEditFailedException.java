package com.teasmile.exception;

/**
 * 修改密码失败异常（一般为原密码不正确）
 */
public class PasswordEditFailedException extends BusinessException {

    public PasswordEditFailedException(String message) {
        super(message);
    }
}
