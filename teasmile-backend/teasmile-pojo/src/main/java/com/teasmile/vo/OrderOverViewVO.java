package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单各状态总量概览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待接单数量 */
    private Integer waitingOrders;

    /** 待派送数量 */
    private Integer deliveredOrders;

    /** 已完成数量 */
    private Integer completedOrders;

    /** 已取消数量 */
    private Integer cancelledOrders;

    /** 全部订单数量 */
    private Integer allOrders;
}
