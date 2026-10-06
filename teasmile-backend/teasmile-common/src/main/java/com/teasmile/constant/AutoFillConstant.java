package com.teasmile.constant;

/**
 * 公共字段自动填充所反射调用的实体类 setter 方法名。
 * 配合自定义注解与切面完成 create/update 审计字段的统一补齐。
 */
public final class AutoFillConstant {

    private AutoFillConstant() {
    }

    public static final String SET_CREATE_TIME = "setCreateTime";
    public static final String SET_UPDATE_TIME = "setUpdateTime";
    public static final String SET_CREATE_USER = "setCreateUser";
    public static final String SET_UPDATE_USER = "setUpdateUser";
}
