package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单统计报表：每日订单数、有效订单数及区间完成率。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日期序列 */
    private String dateList;

    /** 每日订单数序列，如 260,215,200 */
    private String orderCountList;

    /** 每日有效订单数序列，如 250,210,198 */
    private String validOrderCountList;

    /** 区间订单总数 */
    private Integer totalOrderCount;

    /** 区间有效订单数 */
    private Integer validOrderCount;

    /** 订单完成率 */
    private Double orderCompletionRate;
}
