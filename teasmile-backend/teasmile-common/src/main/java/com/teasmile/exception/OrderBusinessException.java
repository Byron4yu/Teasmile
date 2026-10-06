package com.teasmile.exception;

/**
 * 订单业务异常：订单状态流转非法、订单不存在等
 */
public class OrderBusinessException extends BusinessException {

    public OrderBusinessException(String message) {
        super(message);
    }
}
