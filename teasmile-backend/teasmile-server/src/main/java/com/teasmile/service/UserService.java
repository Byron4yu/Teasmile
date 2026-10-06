package com.teasmile.service;

import com.teasmile.dto.UserLoginDTO;
import com.teasmile.entity.User;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/10 20:42
 * @C端用户相关
 */
public interface UserService {
    /**
     * 微信登陆
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
