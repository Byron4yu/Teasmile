package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品分类实体，对应 category 表。
 * type 用于区分分类挂载的商品形态：1 饮品分类，2 套餐分类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 分类类型：1 饮品，2 套餐 */
    private Integer type;

    /** 分类名称（唯一） */
    private String name;

    /** 展示排序权重，数值越小越靠前 */
    private Integer sort;

    /** 分类状态：0 禁用，1 启用 */
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
