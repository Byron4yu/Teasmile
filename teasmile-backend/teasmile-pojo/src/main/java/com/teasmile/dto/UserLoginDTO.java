package com.teasmile.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C 端微信授权登录入参。
 * code 由小程序端 wx.login() 获取，后端凭此向微信换取 openid。
 */
@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 微信登录临时授权码 */
    private String code;
}
