package com.tzxlearn.reggie.service;

import com.tzxlearn.reggie.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 20357
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-02-23 10:48:05
*/
public interface OrdersService extends IService<Orders> {

    //用户下单
    void submit(Orders orders);
}
