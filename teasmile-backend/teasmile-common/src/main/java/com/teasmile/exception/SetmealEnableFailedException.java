package com.teasmile.exception;

/**
 * 套餐启售失败异常：套餐中包含处于停售状态的饮品时抛出
 */
public class SetmealEnableFailedException extends BusinessException {

    public SetmealEnableFailedException() {
        super();
    }

    public SetmealEnableFailedException(String message) {
        super(message);
    }
}
