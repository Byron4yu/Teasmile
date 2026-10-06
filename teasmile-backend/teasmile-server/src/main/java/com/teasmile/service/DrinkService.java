package com.teasmile.service;

import com.teasmile.dto.DrinkDTO;
import com.teasmile.dto.DrinkPageQueryDTO;
import com.teasmile.entity.Drink;
import com.teasmile.result.PageResult;
import com.teasmile.vo.DrinkVO;

import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/7 15:42
 * @注释
 */
public interface DrinkService {
    /**
     * 保存饮品和口味
     * @param drinkDTO
     */
    public void saveWithFlavor(DrinkDTO drinkDTO);

    /**
     * 饮品分页查询
     * @param drinkPageQueryDTO
     */
    public PageResult pageQuery(DrinkPageQueryDTO drinkPageQueryDTO);

    /**
     * 饮品批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询饮品和口味
     * @param id
     * @return
     */
    DrinkVO getByIdWithFlavor(Long id);

    /**
     * 根据id修改饮品
     * @param drinkDTO
     */
    void updateWithFlavor(DrinkDTO drinkDTO);

    /**
     * 起售停售饮品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类id查询饮品
     * @param categoryId
     * @return
     */
    List<Drink> list(Long categoryId);
    /**
     * 条件查询饮品和口味
     * @param drink
     * @return
     */
    List<DrinkVO> listWithFlavor(Drink drink);
}
