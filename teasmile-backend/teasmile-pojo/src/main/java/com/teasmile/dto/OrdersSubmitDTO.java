package com.teasmile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户提交订单入参
 */
@Data
public class OrdersSubmitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 收货地址簿 id */
    private Long addressBookId;

    /** 支付方式：1 微信，2 支付宝 */
    private Integer payMethod;

    /** 订单备注 */
    private String remark;

    /** 预约送达时间（deliveryStatus 为 0 时使用） */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;

    /** 配送方式：1 立即送出，0 预约时间 */
    private Integer deliveryStatus;

    /** 餐具数量 */
    private Integer tablewareNumber;

    /** 餐具提供方式：1 按餐量提供，0 自定义数量 */
    private Integer tablewareStatus;

    /** 打包费 */
    private Integer packAmount;

    /** 订单总金额（以后端实算结果为准，该值仅用于前端展示回传校验） */
    private BigDecimal amount;
}
