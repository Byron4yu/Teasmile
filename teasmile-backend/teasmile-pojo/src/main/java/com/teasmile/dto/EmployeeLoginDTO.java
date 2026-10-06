package com.teasmile.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理端员工登录入参
 */
@Data
@ApiModel(description = "员工登录请求参数")
public class EmployeeLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("登录用户名")
    private String username;

    @ApiModelProperty("登录密码")
    private String password;
}
