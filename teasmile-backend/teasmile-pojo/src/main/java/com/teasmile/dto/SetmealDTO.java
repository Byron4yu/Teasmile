package com.teasmile.dto;

import com.teasmile.entity.SetmealDrink;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增 / 修改套餐入参。
 * 同时携带套餐包含的饮品明细集合，由业务层分别落库 setmeal 与 setmeal_drink。
 */
@Data
public class SetmealDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（修改时必传） */
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

    /** 库存数量 */
    private Integer stock;

    /** 排序权重 */
    private Integer sort;

    /** 套餐内饮品明细集合 */
    private List<SetmealDrink> setmealDrinks = new ArrayList<>();
}
