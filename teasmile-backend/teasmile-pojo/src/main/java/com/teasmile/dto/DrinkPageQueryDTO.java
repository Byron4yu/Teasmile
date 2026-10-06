package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 饮品分页查询条件（管理端）
 */
@Data
public class DrinkPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码，从 1 开始 */
    private int page;

    /** 每页条数 */
    private int pageSize;

    /** 饮品名称模糊关键字 */
    private String name;

    /** 分类 id */
    private Long categoryId;

    /** 售卖状态：0 停售，1 起售 */
    private Integer status;

    /** 是否热门：0 否，1 是 */
    private Integer hot;
}
