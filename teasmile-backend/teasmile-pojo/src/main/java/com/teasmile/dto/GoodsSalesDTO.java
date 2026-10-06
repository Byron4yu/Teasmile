package com.teasmile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品销量统计结果载体，用于销量排行榜等报表查询。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSalesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品名称（饮品 / 套餐） */
    private String name;

    /** 区间内销量 */
    private Integer number;
}
