package com.teasmile.vo;

import com.teasmile.entity.OrderDetail;
import com.teasmile.entity.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情展示对象，继承订单主表全部字段，
 * 额外携带商品名称拼串与明细集合。
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OrderVO extends Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单内商品名称拼接串，用于列表页简要展示 */
    private String orderDrinks;

    /** 订单明细集合 */
    private List<OrderDetail> orderDetailList;
}
