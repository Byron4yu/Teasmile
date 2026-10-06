package com.teasmile.controller.admin;

import com.teasmile.dto.*;
import com.teasmile.result.PageResult;
import com.teasmile.result.ApiResult;
import com.teasmile.service.OrderService;
import com.teasmile.vo.OrderOverViewVO;
import com.teasmile.vo.OrderStatisticsVO;
import com.teasmile.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/13 16:43
 * @订单管理
 */
@RestController("AdminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "管理端订单管理接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 订单条件查询
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单条件查询")
    public ApiResult<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单条件查询，参数为：{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return ApiResult.success(pageResult);
    }


    /**
     * 各个状态订单数量统计
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态订单统计")
    public ApiResult<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return ApiResult.success(orderStatisticsVO);
    }

    /**
     * 查询订单详细
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详细")
    public ApiResult<OrderVO> detail(@PathVariable("id") Long id){
        OrderVO orderVO=orderService.detail(id);
        return ApiResult.success(orderVO);
    }

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public ApiResult confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return ApiResult.success();
    }


    /**
     * 拒单
     * @param ordersRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public ApiResult reject (@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception{
        orderService.reject(ordersRejectionDTO);
        return ApiResult.success();
    }
    /**
     * 取消订单
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public ApiResult cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);
        return ApiResult.success();
    }

    /**
     * 派送
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public ApiResult delivery(@PathVariable("id") Long id){
        orderService.delivery(id);
        return ApiResult.success();
    }


    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public ApiResult complete(@PathVariable("id") Long id){
        orderService.complete(id);
        return ApiResult.success();
    }








}
