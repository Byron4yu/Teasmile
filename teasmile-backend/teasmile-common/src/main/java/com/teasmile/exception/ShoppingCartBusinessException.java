package com.teasmile.exception;

/**
 * 购物车业务异常，如购物车为空时尝试提交订单
 */
public class ShoppingCartBusinessException extends BusinessException {

    public ShoppingCartBusinessException(String message) {
        super(message);
    }
}
