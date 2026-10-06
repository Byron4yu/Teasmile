package com.teasmile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运营数据概览的时间区间入参
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOverViewQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 统计起始时间（含） */
    private LocalDateTime begin;

    /** 统计截止时间（含） */
    private LocalDateTime end;
}
