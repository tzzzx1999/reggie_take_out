package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.OrderDetail;
import com.tzxlearn.reggie.service.OrderDetailService;
import com.tzxlearn.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-02-23 10:49:18
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




