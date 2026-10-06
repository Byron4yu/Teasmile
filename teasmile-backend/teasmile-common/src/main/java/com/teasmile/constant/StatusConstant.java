package com.teasmile.constant;

/**
 * 通用启用/禁用状态取值（员工、分类、饮品等共用）。
 */
public final class StatusConstant {

    private StatusConstant() {
    }

    /** 启用、在售 */
    public static final Integer ENABLE = 1;

    /** 禁用、停售 */
    public static final Integer DISABLE = 0;
}
