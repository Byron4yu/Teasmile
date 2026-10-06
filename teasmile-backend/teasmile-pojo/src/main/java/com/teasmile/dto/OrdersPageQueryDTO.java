package com.teasmile.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单分页查询条件。
 * 管理端按下单时间区间、状态、订单号、手机号筛选；
 * C 端复用时仅按当前登录用户 id 过滤。
 */
@Data
public class OrdersPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码，从 1 开始 */
    private int page;

    /** 每页条数 */
    private int pageSize;

    /** 订单号 */
    private String number;

    /** 收货人手机号 */
    private String phone;

    /** 订单状态 */
    private Integer status;

    /** 区间起始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /** 区间截止时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 下单会员 id（C 端使用） */
    private Long userId;
}
