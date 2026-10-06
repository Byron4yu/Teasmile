package com.teasmile.exception;

/**
 * 收货地址业务异常，如未选择收货地址直接下单
 */
public class AddressBookBusinessException extends BusinessException {

    public AddressBookBusinessException(String message) {
        super(message);
    }
}
