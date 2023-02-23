package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.ShoppingCart;
import com.tzxlearn.reggie.service.ShoppingCartService;
import com.tzxlearn.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-02-22 16:50:45
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




