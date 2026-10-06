package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工分页查询条件
 */
@Data
public class EmployeePageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 姓名模糊关键字 */
    private String name;

    /** 页码，从 1 开始 */
    private int page;

    /** 每页条数 */
    private int pageSize;
}
