package com.teasmile.exception;

/**
 * 禁止删除异常：当数据仍被其它业务数据引用时抛出（如分类下仍有饮品）
 */
public class DeletionNotAllowedException extends BusinessException {

    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
