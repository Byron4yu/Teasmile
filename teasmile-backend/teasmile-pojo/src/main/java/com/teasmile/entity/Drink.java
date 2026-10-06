package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 饮品商品实体，对应 drink 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drink implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 饮品名称（唯一） */
    private String name;

    /** 所属分类 id */
    private Long categoryId;

    /** 当前售卖价 */
    private BigDecimal price;

    /** 原价（前端划线价展示） */
    private BigDecimal originalPrice;

    /** 商品图片地址 */
    private String image;

    /** 饮品简介 */
    private String description;

    /** 是否热门：0 否，1 是 */
    private Integer hot;

    /** 累计销量 */
    private Integer sales;

    /** 成品库存数量 */
    private Integer stock;

    /** 列表排序权重，数值越小越靠前 */
    private Integer sort;

    /** 售卖状态：0 停售，1 起售 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 创建人 id */
    private Long createUser;

    /** 最后修改人 id */
    private Long updateUser;
}
