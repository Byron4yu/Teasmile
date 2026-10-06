package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 奶茶套餐实体，对应 setmeal 表。
 * 套餐与饮品为多对多关系，明细见 setmeal_drink。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属套餐分类 id（category.type = 2） */
    private Long categoryId;

    /** 套餐名称（唯一） */
    private String name;

    /** 套餐售卖价 */
    private BigDecimal price;

    /** 套餐原价（划线价） */
    private BigDecimal originalPrice;

    /** 售卖状态：0 停售，1 起售 */
    private Integer status;

    /** 套餐描述 */
    private String description;

    /** 套餐封面图片 */
    private String image;

    /** 是否热门：0 否，1 是 */
    private Integer hot;

    /** 累计销量 */
    private Integer sales;

    /** 库存数量 */
    private Integer stock;

    /** 排序权重，数值越小越靠前 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 创建人 id */
    private Long createUser;

    /** 最后修改人 id */
    private Long updateUser;
}
