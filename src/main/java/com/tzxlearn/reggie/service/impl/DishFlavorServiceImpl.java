package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.DishFlavor;
import com.tzxlearn.reggie.service.DishFlavorService;
import com.tzxlearn.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-02-20 19:51:50
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




