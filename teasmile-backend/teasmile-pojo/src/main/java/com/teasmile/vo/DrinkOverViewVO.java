package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 饮品启售 / 停售数量概览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrinkOverViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 起售中的饮品数量 */
    private Integer sold;

    /** 停售中的饮品数量 */
    private Integer discontinued;
}
