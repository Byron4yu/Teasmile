package com.teasmile.dto;

import com.teasmile.entity.OrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单条件查询 / 管理端订单展示入参，支持携带明细集合。
 */
@Data
public class OrdersDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单 id */
    private Long id;

    /** 业务订单号 */
    private String number;

    /** 订单状态 */
    private Integer status;

    /** 下单会员 id */
    private Long userId;

    /** 收货地址簿 id */
    private Long addressBookId;

    /** 下单时间 */
    private LocalDateTime orderTime;

    /** 支付完成时间 */
    private LocalDateTime checkoutTime;

    /** 支付方式：1 微信，2 支付宝 */
    private Integer payMethod;

    /** 实付金额 */
    private BigDecimal amount;

    /** 备注 */
    private String remark;

    /** 会员姓名 */
    private String userName;

    /** 收货人手机号 */
    private String phone;

    /** 收货地址 */
    private String address;

    /** 收货人 */
    private String consignee;

    /** 订单明细集合 */
    private List<OrderDetail> orderDetails;
}
