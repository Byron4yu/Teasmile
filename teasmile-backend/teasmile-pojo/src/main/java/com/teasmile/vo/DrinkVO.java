package com.teasmile.vo;

import com.teasmile.entity.DrinkFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 饮品展示对象：管理端编辑回显与 C 端商品详情共用。
 * 在饮品字段基础上补充分类名称与规格选项集合。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrinkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 饮品名称 */
    private String name;

    /** 所属分类 id */
    private Long categoryId;

    /** 售卖价 */
    private BigDecimal price;

    /** 原价（划线价） */
    private BigDecimal originalPrice;

    /** 商品图片 */
    private String image;

    /** 饮品描述 */
    private String description;

    /** 是否热门：0 否，1 是 */
    private Integer hot;

    /** 累计销量 */
    private Integer sales;

    /** 库存数量 */
    private Integer stock;

    /** 排序权重 */
    private Integer sort;

    /** 售卖状态：0 停售，1 起售 */
    private Integer status;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 所属分类名称（联表查询补充） */
    private String categoryName;

    /** 规格选项集合（甜度 / 冰度 / 加料） */
    private List<DrinkFlavor> flavors = new ArrayList<>();
}
