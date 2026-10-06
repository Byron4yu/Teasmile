package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增 / 修改员工时的提交参数。
 * 不含密码与状态：初始密码由系统统一发放，启停通过独立接口控制。
 */
@Data
public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（修改时必传） */
    private Long id;

    /** 登录用户名 */
    private String username;

    /** 员工姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 性别 */
    private String sex;

    /** 身份证号 */
    private String idNumber;
}
