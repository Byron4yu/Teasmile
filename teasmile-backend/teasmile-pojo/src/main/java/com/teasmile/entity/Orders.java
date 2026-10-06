package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体，对应 orders 表。
 * 收货人、手机号、地址等均为下单瞬间的快照，会员事后修改地址不影响历史订单。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    // ---------------- 订单流转状态 ----------------

    /** 待付款 */
    public static final Integer PENDING_PAYMENT = 1;
    /** 待接单 */
    public static final Integer TO_BE_CONFIRMED = 2;
    /** 已接单 */
    public static final Integer CONFIRMED = 3;
    /** 派送中 */
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    /** 已完成 */
    public static final Integer COMPLETED = 5;
    /** 已取消 */
    public static final Integer CANCELLED = 6;
    /** 已退款 */
    public static final Integer REFUNDED = 7;

    // ---------------- 支付状态 ----------------

    /** 未支付 */
    public static final Integer UN_PAID = 0;
    /** 已支付 */
    public static final Integer PAID = 1;
    /** 已退款 */
    public static final Integer REFUND = 2;

    /** 主键 */
    private Long id;

    /** 业务订单号 */
    private String number;

    /** 订单状态，取值见本类状态常量 */
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

    /** 支付状态：0 未支付，1 已支付，2 已退款 */
    private Integer payStatus;

    /** 实付金额 */
    private BigDecimal amount;

    /** 订单备注 */
    private String remark;

    /** 收货人姓名（快照） */
    private String consignee;

    /** 收货人手机号（快照） */
    private String phone;

    /** 完整收货地址（快照） */
    private String address;

    /** 会员姓名（快照） */
    private String userName;

    /** 用户取消原因 */
    private String cancelReason;

    /** 商家拒单原因 */
    private String rejectionReason;

    /** 取消时间 */
    private LocalDateTime cancelTime;

    /** 预计送达时间 */
    private LocalDateTime estimatedDeliveryTime;

    /** 配送方式：1 立即送出，0 预约时间 */
    private Integer deliveryStatus;

    /** 实际送达时间 */
    private LocalDateTime deliveryTime;

    /** 打包费（单位：分） */
    private Integer packAmount;

    /** 餐具数量 */
    private Integer tablewareNumber;

    /** 餐具提供方式：1 按餐量提供，0 自定义数量 */
    private Integer tablewareStatus;
}
