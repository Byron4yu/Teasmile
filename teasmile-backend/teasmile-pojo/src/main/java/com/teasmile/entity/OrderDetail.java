package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细实体，对应 order_detail 表。
 * 记录订单中每一款商品的下单快照，商品后续改名、改价均不影响历史订单展示。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 商品名称（快照） */
    private String name;

    /** 商品图片（快照） */
    private String image;

    /** 所属订单 id */
    private Long orderId;

    /** 饮品 id，单品下单时非空 */
    private Long drinkId;

    /** 套餐 id，套餐下单时非空 */
    private Long setmealId;

    /** 下单选择的规格快照 JSON，套餐明细可为空 */
    private String drinkFlavor;

    /** 购买份数 */
    private Integer number;

    /** 本明细小计金额 */
    private BigDecimal amount;
}
