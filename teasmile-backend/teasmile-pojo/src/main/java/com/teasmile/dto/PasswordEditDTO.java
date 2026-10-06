package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工修改密码入参
 */
@Data
public class PasswordEditDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 员工 id */
    private Long empId;

    /** 原密码 */
    private String oldPassword;

    /** 新密码 */
    private String newPassword;
}
