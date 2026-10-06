package com.teasmile.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.teasmile.constant.MessageConstant;
import com.teasmile.constant.StatusConstant;
import com.teasmile.dto.SetmealDTO;
import com.teasmile.dto.SetmealPageQueryDTO;
import com.teasmile.entity.Drink;
import com.teasmile.entity.DrinkFlavor;
import com.teasmile.entity.Setmeal;
import com.teasmile.entity.SetmealDrink;
import com.teasmile.exception.DeletionNotAllowedException;
import com.teasmile.exception.SetmealEnableFailedException;
import com.teasmile.mapper.DrinkMapper;
import com.teasmile.mapper.SetmealDrinkMapper;
import com.teasmile.mapper.SetmealMapper;
import com.teasmile.result.PageResult;
import com.teasmile.service.SetmealService;
import com.teasmile.vo.DrinkItemVO;
import com.teasmile.vo.DrinkVO;
import com.teasmile.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/8 13:45
 * @注释
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDrinkMapper setmealDrinkMapper;
    @Autowired
    private DrinkMapper drinkMapper;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增套餐，同时保存其包含的饮品
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDrink(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);

        if (setmeal.getSales() == null) {
            setmeal.setSales(0);
        }
        if (setmeal.getImage() == null) {
            setmeal.setImage("");
        }

        setmealMapper.insert(setmeal);
        Long setmealId=setmeal.getId();//插入套餐后获取对应id

        List<SetmealDrink> setmealDrinkList=setmealDTO.getSetmealDrinks();
        if(setmealDrinkList!=null&&setmealDrinkList.size()>0){
            setmealDrinkList.forEach(setmealDrink -> {
                setmealDrink.setSetmealId(setmealId);
            });
            setmealDrinkMapper.insertBatch(setmealDrinkList);//循环向套餐饮品关系设置套餐id后批量插入
        }



    }

    /**
     * 套餐批量删除
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断是否能删除
        //1.套餐是否在售
        for (Long id : ids) {
            Setmeal setmeal=setmealMapper.getById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                //在售
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //2.删除套餐饮品关系表数据
        ids.forEach(setmealId -> {
            //删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            //删除套餐饮品关系表中的数据
            setmealDrinkMapper.deleteBySetmealId(setmealId);
        });

    }

    /**
     * 根据id查询套餐和其关联的饮品
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDrink(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDrink> setmealDrinks = setmealDrinkMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDrinks(setmealDrinks);

        return setmealVO;
    }

    /**
     * 修改套餐及其饮品
     * @param setmealDTO
     */
    public void updateWithDrink(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //修改套餐表基本信息
        setmealMapper.update(setmeal);

        //删除原有的饮品数据
        setmealDrinkMapper.deleteBySetmealId(setmealDTO.getId());

        //重新插入饮品数据
        List<SetmealDrink> setmealDrinks = setmealDTO.getSetmealDrinks();
        if (setmealDrinks != null && setmealDrinks.size() > 0) {
            setmealDrinks.forEach(setmealDrink -> {
                setmealDrink.setSetmealId(setmealDTO.getId());
            });
            //向口味表插入n条数据
            setmealDrinkMapper.insertBatch(setmealDrinks);
        }
    }

    /**
     * 起售停售套餐
     * @param status
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {

        if (status == StatusConstant.ENABLE) {
            // 如果是起售操作，如果有不能起售的饮品则起售失败
            List<Drink> drinkList = drinkMapper.getBySetmealId(id);
            if(drinkList != null && drinkList.size() > 0){
                drinkList.forEach(drink -> {
                    if(StatusConstant.DISABLE == drink.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal=Setmeal.builder().status(status).id(id).build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询饮品选项
     * @param id
     * @return
     */
    public List<DrinkItemVO> getDrinkItemById(Long id) {
        return setmealMapper.getDrinkItemBySetmealId(id);
    }


}
