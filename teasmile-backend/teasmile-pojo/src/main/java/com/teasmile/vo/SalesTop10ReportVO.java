package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品销量 Top10 排行榜报表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesTop10ReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 商品名称序列，如 杨枝甘露,冰鲜柠檬水,生椰拿铁 */
    private String nameList;

    /** 对应销量序列，如 900,900,890 */
    private String numberList;
}
