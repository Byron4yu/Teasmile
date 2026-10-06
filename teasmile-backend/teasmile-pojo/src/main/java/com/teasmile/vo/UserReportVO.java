package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 会员统计报表：每日总量与新增量。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日期序列 */
    private String dateList;

    /** 每日会员总量序列，如 200,210,220 */
    private String totalUserList;

    /** 每日新增会员序列，如 20,10,8 */
    private String newUserList;
}
