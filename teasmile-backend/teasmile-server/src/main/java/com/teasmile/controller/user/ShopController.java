package com.teasmile.controller.user;

import com.teasmile.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/9 16:22
 * @店铺管理
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY="SHOP_STATUS";


    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public ApiResult<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("营业状态为：{}",status==1?"营业":"打烊");
        return ApiResult.success(status);
    }

    @GetMapping("/getMerchantInfo")
    @ApiOperation("获取店铺营业状态")
    public ApiResult<String> getMerchantInfo(){
        //应该从数据库取的，这里偷懒
        String phone="12345678901";
        return ApiResult.success(phone);
//        return ApiResult.success(status);
    }
}
