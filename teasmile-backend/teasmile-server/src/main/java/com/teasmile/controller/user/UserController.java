package com.teasmile.controller.user;

import com.teasmile.constant.JwtClaimsConstant;
import com.teasmile.dto.UserLoginDTO;
import com.teasmile.entity.Category;
import com.teasmile.entity.User;
import com.teasmile.properties.JwtProperties;
import com.teasmile.result.ApiResult;
import com.teasmile.service.UserService;
import com.teasmile.utils.JwtUtil;
import com.teasmile.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/10 20:39
 * @注释
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "C端用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登陆")
    public ApiResult<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录：{}",userLoginDTO.getCode());
        User user=userService.wxLogin(userLoginDTO);

        //为用户生成jwt令牌
        Map<String,Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token=JwtUtil.createToken(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);

        UserLoginVO userLoginVO=UserLoginVO.builder().id(user.getId()).openid(user.getOpenid()).token(token).build();
        return ApiResult.success(userLoginVO);
    }

}
