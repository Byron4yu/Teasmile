package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商家拒单入参
 */
@Data
public class OrdersRejectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 id */
    private Long id;

    /** 拒单原因，将同步展示给下单用户 */
    private String rejectionReason;
}
