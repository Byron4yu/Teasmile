package com.teasmile.controller.admin;

import com.teasmile.dto.DrinkDTO;
import com.teasmile.dto.DrinkPageQueryDTO;
import com.teasmile.dto.SetmealDTO;
import com.teasmile.dto.SetmealPageQueryDTO;
import com.teasmile.result.PageResult;
import com.teasmile.result.ApiResult;
import com.teasmile.service.SetmealService;
import com.teasmile.vo.DrinkVO;
import com.teasmile.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/8 13:38
 * @套餐管理
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public ApiResult<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("饮品分页查询:{}",setmealPageQueryDTO);
        PageResult pageResult=setmealService.pageQuery(setmealPageQueryDTO);
        return ApiResult.success(pageResult);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key="#setmealDTO.categoryId")
    public ApiResult save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐:{}",setmealDTO);
        setmealService.saveWithDrink(setmealDTO);
        return ApiResult.success();
    }

    /**
     * 套餐批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("套餐批量删除")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public ApiResult delete(@RequestParam List<Long> ids){
        log.info("套餐批量删除：{}", ids);
        setmealService.deleteBatch(ids);//后绪步骤实现
        return ApiResult.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public ApiResult<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getByIdWithDrink(id);//后绪步骤实现
        return ApiResult.success(setmealVO);

    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public ApiResult update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.updateWithDrink(setmealDTO);
        return ApiResult.success();
    }

    /**
     * 起售停售套餐
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售停售套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public ApiResult startOrStop(@PathVariable Integer status,Long id){
        log.info("起售停售套餐id:{},{}",status,id);
        setmealService.startOrStop(status,id);
        return ApiResult.success();
    }


}
