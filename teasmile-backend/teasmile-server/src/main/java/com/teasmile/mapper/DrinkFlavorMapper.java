package com.teasmile.mapper;

import com.teasmile.entity.DrinkFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/7 15:54
 * @饮品口味
 */
@Mapper
public interface DrinkFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DrinkFlavor> flavors);

    /**
     * 根据饮品id删除对应的口味数据
     * @param drinkId
     */
    @Delete("delete from drink_flavor where drink_id = #{drinkId}")
    void deleteByDrinkId(Long drinkId);

    /**
     * 根据饮品id查询对应的口味数据
     * @param drinkId
     * @return
     */
    @Select("select * from drink_flavor where drink_id = #{drinkId}")
    List<DrinkFlavor> getByDrinkId(Long drinkId);

    /**
     * 根据饮品id集合批量删除关联的口味
     * @param drinkIds
     */
    void deleteByDrinkIds(List<Long> drinkIds);
}
