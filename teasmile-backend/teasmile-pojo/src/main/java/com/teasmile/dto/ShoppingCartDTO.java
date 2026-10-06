package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加购物车入参。
 * 用户 id 从登录上下文获取，数量与金额由后端计算，前端不可信。
 */
@Data
public class ShoppingCartDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 饮品 id，加购单品时传 */
    private Long drinkId;

    /** 套餐 id，加购套餐时传 */
    private Long setmealId;

    /** 选中的规格组合 JSON，如 {"甜度":"半糖","冰度":"少冰"} */
    private String drinkFlavor;
}
