package com.teasmile.service;

import com.teasmile.vo.BusinessDataVO;
import com.teasmile.vo.DrinkOverViewVO;
import com.teasmile.vo.OrderOverViewVO;
import com.teasmile.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/16 13:49
 * @注释
 */
public interface WorkspaceService {


    /**
     * 获取今日数据
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 统计订单
     * @return
     */
    OrderOverViewVO getOverviewOrders();

    /**
     * 统计饮品数量
     * @return
     */
    DrinkOverViewVO getOverviewDrinks();

    /**
     * 统计套餐数量
     * @return
     */
    SetmealOverViewVO getOverviewSetmeals();
}
