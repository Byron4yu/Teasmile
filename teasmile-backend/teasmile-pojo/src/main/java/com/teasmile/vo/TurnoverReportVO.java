package com.teasmile.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 营业额统计报表。
 * 两个字段均为逗号分隔的字符串序列，便于前端 ECharts 直接按索引对位渲染。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverReportVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 日期序列，如 2026-09-01,2026-09-02,2026-09-03 */
    private String dateList;

    /** 每日营业额序列，如 406.0,1520.0,75.0 */
    private String turnoverList;
}
