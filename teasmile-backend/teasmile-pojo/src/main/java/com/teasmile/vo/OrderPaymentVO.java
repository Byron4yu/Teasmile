package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 小程序调起微信支付所需的签名参数集合。
 * 注意：返回前端时 packageStr 需映射为微信规定的字段名 "package"
 * （package 是 Java 关键字，不能直接作为属性名）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 随机字符串 */
    private String nonceStr;

    /** 支付签名 */
    private String paySign;

    /** 时间戳（秒） */
    private String timeStamp;

    /** 签名算法，固定 RSA */
    private String signType;

    /** 统一下单返回的 prepay_id 参数值 */
    private String packageStr;
}
