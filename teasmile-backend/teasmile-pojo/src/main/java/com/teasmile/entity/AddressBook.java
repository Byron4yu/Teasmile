package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 收货地址簿实体，对应 address_book 表。
 * 一个会员可维护多条地址，至多一条为默认地址。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属会员 id */
    private Long userId;

    /** 收货人姓名 */
    private String consignee;

    /** 联系电话 */
    private String phone;

    /** 性别：0 女，1 男 */
    private String sex;

    /** 省级区划编号 */
    private String provinceCode;

    /** 省级名称 */
    private String provinceName;

    /** 市级区划编号 */
    private String cityCode;

    /** 市级名称 */
    private String cityName;

    /** 区级区划编号 */
    private String districtCode;

    /** 区级名称 */
    private String districtName;

    /** 街道门牌号等详细信息 */
    private String detail;

    /** 地址标签，如 家、公司、学校 */
    private String label;

    /** 是否默认地址：0 否，1 是 */
    private Integer isDefault;
}
