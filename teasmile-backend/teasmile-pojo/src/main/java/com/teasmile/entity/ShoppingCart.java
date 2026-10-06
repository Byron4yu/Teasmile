package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车条目实体，对应 shopping_cart 表。
 * 同一用户、同一商品、同一规格组合在表中只保留一条，重复加购累加 number。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 商品名称（下单加购时的冗余快照） */
    private String name;

    /** 商品图片（冗余快照） */
    private String image;

    /** 所属会员 id */
    private Long userId;

    /** 饮品 id，加购单品时非空 */
    private Long drinkId;

    /** 套餐 id，加购套餐时非空 */
    private Long setmealId;

    /** 用户选择的规格快照 JSON，套餐条目可为空 */
    private String drinkFlavor;

    /** 购买数量 */
    private Integer number;

    /** 本条小计金额 */
    private BigDecimal amount;

    /** 加入购物车时间 */
    private LocalDateTime createTime;
}
