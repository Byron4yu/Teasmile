package com.teasmile.enumeration;

/**
 * 持久层操作方式，用于公共字段自动填充切面判断需要补齐的字段。
 */
public enum OperationType {

    /**
     * 新增：创建时间、创建人、更新时间、更新人均需填充
     */
    INSERT,

    /**
     * 修改：仅填充更新时间、更新人
     */
    UPDATE
}
