package com.teasmile.vo;

import com.teasmile.entity.SetmealDrink;
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
 * 套餐展示对象，补充分类名称与套餐内饮品明细集合。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属套餐分类 id */
    private Long categoryId;

    /** 套餐名称 */
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

    /** 排序权重 */
    private Integer sort;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 所属分类名称（联表查询补充） */
    private String categoryName;

    /** 套餐内饮品明细 */
    private List<SetmealDrink> setmealDrinks = new ArrayList<>();
}
