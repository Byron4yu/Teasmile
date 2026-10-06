package com.teasmile.controller.admin;

import com.teasmile.dto.DrinkDTO;
import com.teasmile.dto.DrinkPageQueryDTO;
import com.teasmile.entity.Drink;
import com.teasmile.entity.Employee;
import com.teasmile.result.PageResult;
import com.teasmile.result.ApiResult;
import com.teasmile.service.DrinkService;
import com.teasmile.vo.DrinkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.List;
import java.util.Set;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/7 15:40
 * @饮品管理
 */
@RestController
@RequestMapping("/admin/drink")
@Api(tags = "饮品相关接口")
@Slf4j
public class DrinkController {

    @Autowired
    private DrinkService drinkService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 清理缓存数据
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
    /**
     * 新增饮品
     * @param drinkDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增饮品")
    public ApiResult save(@RequestBody DrinkDTO drinkDTO){
        log.info("新增饮品:{}",drinkDTO);
        drinkService.saveWithFlavor(drinkDTO);

        //清理缓存

        String key="drink_"+drinkDTO.getCategoryId();
        cleanCache(key);
        return ApiResult.success();
    }

    /**
     * 饮品分页查询
     * @param drinkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("饮品分页查询")
    public ApiResult<PageResult> page(DrinkPageQueryDTO drinkPageQueryDTO){
        log.info("饮品分页查询:{}",drinkPageQueryDTO);
        PageResult pageResult=drinkService.pageQuery(drinkPageQueryDTO);
        return ApiResult.success(pageResult);
    }

    /**
     * 饮品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("饮品批量删除")
    public ApiResult delete(@RequestParam List<Long> ids){
        log.info("饮品批量删除：{}", ids);
        drinkService.deleteBatch(ids);//后绪步骤实现
        //将所有的饮品缓存数据清理掉，所有以drink_开头的key
        cleanCache("drink_*");
        return ApiResult.success();
    }

    /**
     * 根据id查询饮品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询饮品")
    public ApiResult<DrinkVO> getById(@PathVariable Long id){
        DrinkVO drinkVO = drinkService.getByIdWithFlavor(id);//后绪步骤实现
        return ApiResult.success(drinkVO);

    }

    /**
     * 修改饮品
     * @param drinkDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改饮品")
    public ApiResult update(@RequestBody DrinkDTO drinkDTO) {
        log.info("修改饮品：{}", drinkDTO);
        drinkService.updateWithFlavor(drinkDTO);

        //将所有的饮品缓存数据清理掉，所有以drink_开头的key
        cleanCache("drink_*");
        return ApiResult.success();
    }

    /**
     * 起售停售饮品
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售停售饮品")
    public ApiResult startOrStop(@PathVariable Integer status,Long id){
        log.info("起售停售饮品id:{},{}",status,id);
        drinkService.startOrStop(status,id);
        //将所有的饮品缓存数据清理掉，所有以drink_开头的key
        cleanCache("drink_*");
        return ApiResult.success();
    }

    /**
     * 根据分类id查询饮品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询饮品")
    public ApiResult<List<Drink>> list(Long categoryId){
        List<Drink> list = drinkService.list(categoryId);
        return ApiResult.success(list);
    }

}
