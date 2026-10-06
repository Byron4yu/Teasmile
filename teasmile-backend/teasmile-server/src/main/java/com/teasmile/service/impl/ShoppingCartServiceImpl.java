package com.teasmile.service.impl;

import com.teasmile.context.LoginContext;
import com.teasmile.dto.ShoppingCartDTO;
import com.teasmile.entity.Drink;
import com.teasmile.entity.Setmeal;
import com.teasmile.entity.ShoppingCart;
import com.teasmile.mapper.DrinkMapper;
import com.teasmile.mapper.SetmealMapper;
import com.teasmile.mapper.ShoppingCartMapper;
import com.teasmile.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/11 14:19
 * @注释
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DrinkMapper drinkMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(LoginContext.getUserId());
        //判断当前商品是否在购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);//查看该商品是否已经在购物车中

        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            //如果已经存在，就更新数量，数量加1
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        } else {
            //如果不存在，插入数据，数量就是1

            //判断当前添加到购物车的是饮品还是套餐
            Long drinkId = shoppingCartDTO.getDrinkId();
            if (drinkId != null) {
                //添加到购物车的是饮品
                Drink drink = drinkMapper.getById(drinkId);
                shoppingCart.setName(drink.getName());
                shoppingCart.setImage(drink.getImage());
                shoppingCart.setAmount(drink.getPrice());
            } else {
                //添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }


    }

    /**
     * 查看购物车
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        return shoppingCartMapper.list(ShoppingCart.
                builder().
                userId(LoginContext.getUserId()).
                build());
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        shoppingCartMapper.deleteByUserId(LoginContext.getUserId());
    }

    /**
     * 减少购物车商品数量（update/del)
     * @param shoppingCartDTO
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //只能查询自己的购物车数据
        shoppingCart.setUserId(LoginContext.getUserId());
        //判断当前商品是否在购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);//查看该商品是否已经在购物车中
        //如果已经存在，就更新数量，数量大于1，则减一，若只有一个，则删除该记录
        if (shoppingCartList != null&&shoppingCartList.size() == 1) {
            shoppingCart = shoppingCartList.get(0);
            if(shoppingCart.getNumber()==1){
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else{
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
