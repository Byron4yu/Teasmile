package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 套餐详情中内嵌的饮品摘要对象，仅保留展示所需的最少字段。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrinkItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 饮品名称 */
    private String name;

    /** 套餐内份数 */
    private Integer copies;

    /** 饮品图片 */
    private String image;

    /** 饮品描述 */
    private String description;
}
