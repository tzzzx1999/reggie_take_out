package com.tzxlearn.reggie.controller;

import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.Orders;
import com.tzxlearn.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: tzx
 * @time: 2023/2/23 10:51
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;


    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders) {

        ordersService.submit(orders);
        return R.success("下单成功");
    }
}
