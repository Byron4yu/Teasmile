package com.teasmile.controller.user;

import com.teasmile.constant.StatusConstant;
import com.teasmile.entity.Drink;
import com.teasmile.result.ApiResult;
import com.teasmile.service.DrinkService;
import com.teasmile.vo.DrinkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDrinkController")
@RequestMapping("/user/drink")
@Slf4j
@Api(tags = "C端-饮品浏览接口")
public class DrinkController {
    @Autowired
    private DrinkService drinkService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询饮品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询饮品")
    public ApiResult<List<DrinkVO>> list(Long categoryId) {
        //构造redis的key drink_分类id
        String key="drink_"+categoryId;
        //查询redis是否有缓存
        List<DrinkVO> list=(List<DrinkVO>)redisTemplate.opsForValue().get(key);
        if(list!=null&&list.size()>0){
            return ApiResult.success(list);
        }

        Drink drink = new Drink();
        drink.setCategoryId(categoryId);
        drink.setStatus(StatusConstant.ENABLE);//查询起售中的饮品
        //  不存在则查库并放入redis
        list = drinkService.listWithFlavor(drink);
        redisTemplate.opsForValue().set(key,list);

        return ApiResult.success(list);
    }

}
