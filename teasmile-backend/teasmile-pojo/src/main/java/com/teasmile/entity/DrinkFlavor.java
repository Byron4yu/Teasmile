package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 饮品规格选项实体，对应 drink_flavor 表。
 * name 为规格维度（甜度 / 冰度 / 加料），
 * value 存储该维度下的可选值，格式为 JSON 数组字符串，
 * 例如：["无糖","少糖","半糖","多糖","全糖"]。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DrinkFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属饮品 id */
    private Long drinkId;

    /** 规格维度名称：甜度 / 冰度 / 加料 */
    private String name;

    /** 可选值 JSON 数组字符串 */
    private String value;
}
