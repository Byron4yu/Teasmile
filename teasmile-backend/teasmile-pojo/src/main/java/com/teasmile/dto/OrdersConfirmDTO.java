package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商家接单入参，可携带接单后的目标状态（已接单 / 派送中）
 */
@Data
public class OrdersConfirmDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 id */
    private Long id;

    /** 目标订单状态：3 已接单，4 派送中 */
    private Integer status;
}
