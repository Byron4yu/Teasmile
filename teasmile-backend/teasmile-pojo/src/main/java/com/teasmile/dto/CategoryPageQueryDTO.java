package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分类分页查询条件
 */
@Data
public class CategoryPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码，从 1 开始 */
    private int page;

    /** 每页条数 */
    private int pageSize;

    /** 分类名称模糊关键字 */
    private String name;

    /** 分类类型：1 饮品分类，2 套餐分类 */
    private Integer type;
}
