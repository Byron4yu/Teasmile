package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 发起支付入参
 */
@Data
public class OrdersPaymentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 业务订单号 */
    private String orderNumber;

    /** 支付方式：1 微信，2 支付宝 */
    private Integer payMethod;
}
