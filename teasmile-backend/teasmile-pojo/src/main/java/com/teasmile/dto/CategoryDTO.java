package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增 / 修改商品分类入参
 */
@Data
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（修改时必传） */
    private Long id;

    /** 分类类型：1 饮品分类，2 套餐分类 */
    private Integer type;

    /** 分类名称 */
    private String name;

    /** 排序权重 */
    private Integer sort;
}
