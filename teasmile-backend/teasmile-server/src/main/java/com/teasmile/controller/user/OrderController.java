package com.teasmile.controller.user;

import com.alibaba.fastjson.JSON;
import com.teasmile.dto.OrdersPageQueryDTO;
import com.teasmile.dto.OrdersPaymentDTO;
import com.teasmile.dto.OrdersSubmitDTO;
import com.teasmile.entity.OrderDetail;
import com.teasmile.result.PageResult;
import com.teasmile.result.ApiResult;
import com.teasmile.service.OrderService;
import com.teasmile.vo.OrderPaymentVO;
import com.teasmile.vo.OrderSubmitVO;
import com.teasmile.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @Author Imak
 * @Date 2024/8/12 15:01
 * @订单管理
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public ApiResult<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return ApiResult.success(orderSubmitVO);
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public ApiResult<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return ApiResult.success(orderPaymentVO);
    }

    /**
     * 订单详细信息
     * @param id
     * @return
     */
    // TODO 开发订单详细页
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单详细信息")
    public ApiResult<OrderVO> orderDetail(@PathVariable("id") Long id){
        OrderVO orderVO=orderService.detail(id);
        return ApiResult.success(orderVO);
    }


    /**
     * 历史订单列表
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public ApiResult<PageResult> page(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult=orderService.pageQuery(ordersPageQueryDTO);
        return ApiResult.success(pageResult);
    }

    /**
     * 根据id催单
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public ApiResult reminder(@PathVariable("id") Long id){
        log.info("该用户向商家催单:{}",id);
        orderService.reminder(id);
        return ApiResult.success();
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public ApiResult repetition(@PathVariable("id") Long id){
        orderService.repetition(id);
        return ApiResult.success();
    }

    /**
     * 拒单
     * @param id
     * @return
     * @throws Exception
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("拒单")
    public ApiResult cancel(@PathVariable("id") Long id)throws Exception{
        orderService.cancelOrder(id);
        return ApiResult.success();
    }




}
