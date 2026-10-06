package com.teasmile.mapper;

import com.github.pagehelper.Page;
import com.teasmile.annotation.AutoFill;
import com.teasmile.dto.DrinkPageQueryDTO;
import com.teasmile.entity.Drink;
import com.teasmile.enumeration.OperationType;
import com.teasmile.vo.DrinkVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/6 20:27
 * @注释
 */

/**
 * 饮品的持久层
 */
@Mapper
public interface DrinkMapper {

    /**
     * 根据分类的id查询其是否有关联的饮品(数量)
     * @param id
     * @return
     */
    @Select("select count(id) from drink where category_id=#{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 新增饮品
     * @param drink
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Drink drink);

    /**
     * 饮品分页查询
     * @param drinkPageQueryDTO
     * @return
     */
    Page<DrinkVO> pageQuery(DrinkPageQueryDTO drinkPageQueryDTO);

    /**
     * 根据id查询饮品
     * @param id
     * @return
     */
    @Select("select * from drink where id = #{id}")
    Drink getById(Long id);

    /**
     * 根据主键删除饮品数据
     * @param id
     */
    @Delete("delete from drink where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据id集合批量删除
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 修改饮品信息
     * @param drink
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Drink drink);

    /**
     * 动态条件查询饮品
     * @param drink
     * @return
     */
    List<Drink> list(Drink drink);

    /**
     * 根据套餐id查询其饮品
     * @param setmealId
     * @return
     */
    @Select("select a.* from drink a left join setmeal_drink b on a.id = b.drink_id where b.setmeal_id = #{setmealId}")
    List<Drink> getBySetmealId(Long setmealId);

    /**
     * 根据状态查询饮品数量
     * @param status
     * @return
     */
    @Select("select count(id) from drink where status=#{status}")
    Integer countByStatus(Integer status);

    /**
     * 累加饮品销量。
     * number 为正表示销量增加（支付成功），为负表示销量回滚（订单取消/退款）。
     * @param drinkId 饮品 id
     * @param number  变动数量（可正可负）
     */
    @Update("update drink set sales = sales + #{number} where id = #{drinkId}")
    void updateSales(@Param("drinkId") Long drinkId, @Param("number") Integer number);
}
