package com.teasmile.service;

import com.teasmile.dto.SetmealDTO;
import com.teasmile.dto.SetmealPageQueryDTO;
import com.teasmile.entity.Setmeal;
import com.teasmile.result.PageResult;
import com.teasmile.vo.DrinkItemVO;
import com.teasmile.vo.SetmealVO;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/8 13:40
 * @注释
 */
public interface SetmealService {
    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveWithDrink(SetmealDTO setmealDTO);

    /**
     * 套餐批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐及关联饮品
     * @param id
     * @return
     */
    SetmealVO getByIdWithDrink(Long id);

    /**
     * 修改套餐和饮品
     * @param setmealDTO
     */
    void updateWithDrink(SetmealDTO setmealDTO);

    /**
     * 起售停售套餐
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询饮品选项
     * @param id
     * @return
     */
    List<DrinkItemVO> getDrinkItemById(Long id);
}
