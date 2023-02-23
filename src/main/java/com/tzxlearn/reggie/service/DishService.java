package com.tzxlearn.reggie.service;

import com.tzxlearn.reggie.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tzxlearn.reggie.dto.DishDto;

import java.util.List;

/**
 * @author 20357
 * @description 针对表【dish(菜品管理)】的数据库操作Service
 * @createDate 2023-02-20 15:16:56
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时将菜品对应的口味数据放入到口味表中
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息
    DishDto querySingleDishDto(Long id);

    //修改菜品
    void updateWithFlavor(DishDto dishDto);

    //删除菜品信息
    void delDishAndFlavor(List<Long> idList);
}
