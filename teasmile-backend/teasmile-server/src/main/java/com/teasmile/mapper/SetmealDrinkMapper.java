package com.teasmile.mapper;

import com.teasmile.entity.SetmealDrink;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/7 16:53
 * @饮品和套餐的关系表
 */
@Mapper
public interface SetmealDrinkMapper {
    /**
     * 根据饮品id查询对应的套餐id
     * @param drinkIds
     * @return
     */
    //select setmeal_id from setmeal_drink where drink_id in (1,2,3,4)
    List<Long> getSetmealIdsByDrinkIds(List<Long> drinkIds);

    /**
     * 批量插入套餐的饮品
     * @param setmealDrinkList
     */
    void insertBatch(List<SetmealDrink> setmealDrinkList);

    /**
     * 根据套餐id删除套餐和饮品的关联关系
     * @param setmealId
     */
    @Delete("delete from setmeal_drink where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询套餐和饮品的关联关系
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_drink where setmeal_id = #{setmealId}")
    List<SetmealDrink> getBySetmealId(Long setmealId);
}