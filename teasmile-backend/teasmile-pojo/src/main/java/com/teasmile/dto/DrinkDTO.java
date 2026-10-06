package com.teasmile.dto;

import com.teasmile.entity.DrinkFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增 / 修改饮品入参。
 * 同时携带饮品的规格选项集合，由业务层分别落库 drink 与 drink_flavor 两张表。
 */
@Data
public class DrinkDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（修改时必传） */
    private Long id;

    /** 饮品名称 */
    private String name;

    /** 所属分类 id */
    private Long categoryId;

    /** 售卖价 */
    private BigDecimal price;

    /** 原价（划线价） */
    private BigDecimal originalPrice;

    /** 商品图片地址 */
    private String image;

    /** 饮品描述 */
    private String description;

    /** 是否热门：0 否，1 是 */
    private Integer hot;

    /** 库存数量 */
    private Integer stock;

    /** 排序权重 */
    private Integer sort;

    /** 售卖状态：0 停售，1 起售 */
    private Integer status;

    /** 甜度 / 冰度 / 加料等规格选项集合 */
    private List<DrinkFlavor> flavors = new ArrayList<>();
}
