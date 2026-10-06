package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐-饮品关联实体，对应 setmeal_drink 表。
 * 描述某个套餐中包含哪款饮品、包含几份。
 * name/price 为冗余快照，避免展示套餐明细时再关联 drink 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDrink implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 套餐 id */
    private Long setmealId;

    /** 饮品 id */
    private Long drinkId;

    /** 饮品名称（冗余快照） */
    private String name;

    /** 加入套餐时的饮品单价（冗余快照） */
    private BigDecimal price;

    /** 该饮品在套餐中的份数 */
    private Integer copies;
}
