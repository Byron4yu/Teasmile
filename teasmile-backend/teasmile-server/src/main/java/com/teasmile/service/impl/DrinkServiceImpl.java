package com.teasmile.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.teasmile.constant.MessageConstant;
import com.teasmile.constant.StatusConstant;
import com.teasmile.dto.DrinkDTO;
import com.teasmile.dto.DrinkPageQueryDTO;
import com.teasmile.entity.Drink;
import com.teasmile.entity.DrinkFlavor;
import com.teasmile.entity.Setmeal;
import com.teasmile.exception.DeletionNotAllowedException;
import com.teasmile.mapper.*;
import com.teasmile.result.PageResult;
import com.teasmile.service.DrinkService;
import com.teasmile.vo.DrinkVO;
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
 * @Date 2024/8/7 15:44
 * @饮品业务实现类
 */
@Service
@Slf4j
public class DrinkServiceImpl implements DrinkService {
    @Autowired
    private DrinkMapper drinkMapper;

    @Autowired
    private DrinkFlavorMapper drinkFlavorMapper;

    @Autowired
    private SetmealDrinkMapper setmealDrinkMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 新增饮品和口味
     * @param drinkDTO
     */
    @Transactional
    public void saveWithFlavor(DrinkDTO drinkDTO) {
        Drink drink=new Drink();
        BeanUtils.copyProperties(drinkDTO,drink);
        //向饮品表插入数据
        drinkMapper.insert(drink);
        Long drinkId = drink.getId();//获取insert语句生成的主键值

        //向口味表插入n条数据
        List<DrinkFlavor> flavors = drinkDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){
            flavors.forEach(drinkFlavor -> {
                drinkFlavor.setDrinkId(drinkId);
            });
            drinkFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 饮品分页查询
     * @param drinkPageQueryDTO
     */
    public PageResult pageQuery(DrinkPageQueryDTO drinkPageQueryDTO) {
        PageHelper.startPage(drinkPageQueryDTO.getPage(),drinkPageQueryDTO.getPageSize());
        Page<DrinkVO> page=drinkMapper.pageQuery(drinkPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());

    }

    /**
     * 饮品批量删除,(多个表操作，使用事务注解保证一致性@Transactional)
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {

        //判断是否能删除
        //1.饮品是否在售
        for (Long id : ids) {
            Drink drink=drinkMapper.getById(id);
            if(drink.getStatus()== StatusConstant.ENABLE){
                //在售
                throw new DeletionNotAllowedException(MessageConstant.DRINK_ON_SALE);
            }
        }

        //2.饮品是否关联套餐、
        List<Long> setmealIds=setmealDrinkMapper.getSetmealIdsByDrinkIds(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DRINK_BE_RELATED_BY_SETMEAL);
        }

        //批量删除饮品
        drinkMapper.deleteByIds(ids);

        for(Long drinkId:ids){
            drinkFlavorMapper.deleteByDrinkId(drinkId);
        }

    }

    /**
     * 根据id查询饮品和口味
     * @param id
     * @return
     */
    public DrinkVO getByIdWithFlavor(Long id) {
        //根据id查询饮品数据
        Drink drink = drinkMapper.getById(id);

        //根据饮品id查询口味数据
        List<DrinkFlavor> drinkFlavors = drinkFlavorMapper.getByDrinkId(id);//后绪步骤实现

        //将查询到的数据封装到VO
        DrinkVO drinkVO = new DrinkVO();
        BeanUtils.copyProperties(drink, drinkVO);
        drinkVO.setFlavors(drinkFlavors);

        return drinkVO;
    }

    /**
     * 根据id修改饮品基本信息和对应的口味信息
     * @param drinkDTO
     */
    public void updateWithFlavor(DrinkDTO drinkDTO) {
        Drink drink = new Drink();
        BeanUtils.copyProperties(drinkDTO, drink);

        //修改饮品表基本信息
        drinkMapper.update(drink);

        //删除原有的口味数据
        drinkFlavorMapper.deleteByDrinkId(drinkDTO.getId());

        //重新插入口味数据
        List<DrinkFlavor> flavors = drinkDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(drinkFlavor -> {
                drinkFlavor.setDrinkId(drinkDTO.getId());
            });
            //向口味表插入n条数据
            drinkFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 起售停售饮品
     * @param status
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Drink drink=Drink.builder().status(status).id(id).build();
        drinkMapper.update(drink);
        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前饮品的套餐也停售
            List<Long> drinkIds = new ArrayList<>();
            drinkIds.add(id);
            // select setmeal_id from setmeal_drink where drink_id in (?,?,?)
            List<Long> setmealIds = setmealDrinkMapper.getSetmealIdsByDrinkIds(drinkIds);//利用旧的方法，要求传入一个List数组
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据分类id查询饮品
     * @param categoryId
     * @return
     */
    public List<Drink> list(Long categoryId) {
        Drink drink = Drink.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return drinkMapper.list(drink);
    }

    /**
     * 条件查询饮品和口味
     * @param drink
     * @return
     */
    public List<DrinkVO> listWithFlavor(Drink drink) {
        List<Drink> drinkList = drinkMapper.list(drink);

        List<DrinkVO> drinkVOList = new ArrayList<>();

        for (Drink d : drinkList) {
            DrinkVO drinkVO = new DrinkVO();
            BeanUtils.copyProperties(d,drinkVO);

            //根据饮品id查询对应的口味
            List<DrinkFlavor> flavors = drinkFlavorMapper.getByDrinkId(d.getId());

            drinkVO.setFlavors(flavors);
            drinkVOList.add(drinkVO);
        }

        return drinkVOList;
    }
}
