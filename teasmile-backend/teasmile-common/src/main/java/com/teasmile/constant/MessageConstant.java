package com.teasmile.constant;

/**
 * 业务提示语常量，统一维护返回给前端的文案，避免散落在各业务类中。
 */
public final class MessageConstant {

    private MessageConstant() {
    }

    // ---------------- 账号与登录 ----------------

    public static final String PASSWORD_ERROR = "登录密码不正确";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_LOCKED = "账号已被禁用，请联系管理员";
    public static final String LOGIN_FAILED = "登录失败，请稍后重试";
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String PASSWORD_EDIT_FAILED = "原密码校验未通过，密码修改失败";

    // ---------------- 通用数据校验 ----------------

    public static final String ALREADY_EXISTS = "数据已存在";
    public static final String UNKNOWN_ERROR = "系统繁忙，请稍后再试";
    public static final String UPLOAD_FAILED = "图片上传失败";
    public static final String STOCK_NOT_ENOUGH = "商品库存不足，请调整购买数量";

    // ---------------- 分类与饮品 ----------------

    public static final String CATEGORY_BE_RELATED_BY_DRINK = "该分类下仍有饮品，无法删除";
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "该分类下仍有套餐，无法删除";
    public static final String DRINK_ON_SALE = "饮品正在售卖中，不能删除";
    public static final String DRINK_BE_RELATED_BY_SETMEAL = "当前饮品已被套餐关联，不能删除";
    public static final String SETMEAL_ON_SALE = "套餐正在售卖中，不能删除";
    public static final String SETMEAL_ENABLE_FAILED = "套餐内包含停售饮品，无法启售";

    // ---------------- 下单流程 ----------------

    public static final String SHOPPING_CART_IS_NULL = "购物车为空，无法提交订单";
    public static final String ADDRESS_BOOK_IS_NULL = "请先选择收货地址";
    public static final String ORDER_STATUS_ERROR = "当前订单状态不允许执行该操作";
    public static final String ORDER_NOT_FOUND = "订单不存在或已失效";
}
