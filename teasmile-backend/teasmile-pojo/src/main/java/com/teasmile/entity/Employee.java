package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后台员工账号实体，对应 employee 表。
 * 注意：password 字段仅用于持久化与登录校验，严禁随接口明文返回前端。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 登录用户名（唯一） */
    private String username;

    /** 员工姓名 */
    private String name;

    /** 登录密码（密文存储） */
    private String password;

    /** 手机号 */
    private String phone;

    /** 性别 */
    private String sex;

    /** 身份证号 */
    private String idNumber;

    /** 账号状态：0 禁用，1 启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后修改时间 */
    private LocalDateTime updateTime;

    /** 创建人 id */
    private Long createUser;

    /** 最后修改人 id */
    private Long updateUser;
}
