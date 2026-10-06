package com.teasmile.constant;

/**
 * JWT 载荷（Claims）中使用的键名常量，管理端与用户端共用。
 */
public final class JwtClaimsConstant {

    private JwtClaimsConstant() {
    }

    /** 管理端员工 id */
    public static final String EMP_ID = "empId";
    /** C 端会员 id */
    public static final String USER_ID = "userId";
    /** 手机号 */
    public static final String PHONE = "phone";
    /** 用户名 */
    public static final String USERNAME = "username";
    /** 姓名/昵称 */
    public static final String NAME = "name";
}
