package com.teasmile.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端订单各状态数量统计（工作台来单提醒角标）
 */
@Data
public class OrderStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 待接单数量 */
    private Integer toBeConfirmed;

    /** 待派送（已接单）数量 */
    private Integer confirmed;

    /** 派送中数量 */
    private Integer deliveryInProgress;
}
