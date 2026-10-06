package com.teasmile.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * C 端微信会员实体，对应 user 表。
 * 用户首次通过小程序登录时按 openid 自动建档。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 微信平台下的用户唯一标识 */
    private String openid;

    /** 昵称/姓名（授权前可能为空） */
    private String name;

    /** 手机号 */
    private String phone;

    /** 性别：0 女，1 男 */
    private String sex;

    /** 身份证号 */
    private String idNumber;

    /** 头像地址 */
    private String avatar;

    /** 注册时间 */
    private LocalDateTime createTime;
}
