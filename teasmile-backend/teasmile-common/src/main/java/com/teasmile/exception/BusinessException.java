package com.teasmile.exception;

/**
 * 业务异常基类。
 * <p>
 * 所有可预期的业务校验失败均抛出该异常（或其子类），
 * 由全局异常处理器统一捕获并转换为 ApiResult.error(...) 返回前端。
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
