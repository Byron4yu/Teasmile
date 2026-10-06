package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 取消订单入参（用户主动取消或管理员代为取消）
 */
@Data
public class OrdersCancelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 id */
    private Long id;

    /** 取消原因 */
    private String cancelReason;
}
