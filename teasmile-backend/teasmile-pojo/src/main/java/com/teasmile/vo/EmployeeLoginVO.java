package com.teasmile.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理端员工登录成功返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录成功返回数据")
public class EmployeeLoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工 id")
    private Long id;

    @ApiModelProperty("登录用户名")
    private String userName;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("JWT 访问令牌")
    private String token;
}
