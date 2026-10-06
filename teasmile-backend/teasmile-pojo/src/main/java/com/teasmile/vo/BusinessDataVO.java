package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 今日运营数据概览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 营业额（元） */
    private Double turnover;

    /** 有效订单数 */
    private Integer validOrderCount;

    /** 订单完成率 */
    private Double orderCompletionRate;

    /** 平均客单价（元） */
    private Double unitPrice;

    /** 新增会员数 */
    private Integer newUsers;
}
