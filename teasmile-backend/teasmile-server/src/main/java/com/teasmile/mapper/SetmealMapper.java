package com.teasmile.mapper;

import com.github.pagehelper.Page;
import com.teasmile.annotation.AutoFill;
import com.teasmile.dto.SetmealPageQueryDTO;
import com.teasmile.entity.Setmeal;
import com.teasmile.enumeration.OperationType;
import com.teasmile.vo.DrinkItemVO;
import com.teasmile.vo.DrinkVO;
import com.teasmile.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/6 20:38
 * @注释
 */

/**
 * 套餐的持久层
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id=#{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据id修改套餐
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据id删除套餐
     * @param setmealId
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询饮品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_drink sd left join drink d on sd.drink_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DrinkItemVO> getDrinkItemBySetmealId(Long setmealId);

    /**
     * 根据状态查询套餐数量
     * @param status
     * @return
     */
    @Select("select count(id) from setmeal where status=#{status}")
    Integer countByStatus(Integer status);

    /**
     * 累加套餐销量。
     * number 为正表示销量增加（支付成功），为负表示销量回滚（订单取消/退款）。
     * @param setmealId 套餐 id
     * @param number    变动数量（可正可负）
     */
    @Update("update setmeal set sales = sales + #{number} where id = #{setmealId}")
    void updateSales(@Param("setmealId") Long setmealId, @Param("number") Integer number);
}
