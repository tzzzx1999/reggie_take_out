package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.common.CustomException;
import com.tzxlearn.reggie.domain.Category;
import com.tzxlearn.reggie.domain.Dish;
import com.tzxlearn.reggie.domain.Setmeal;
import com.tzxlearn.reggie.service.CategoryService;
import com.tzxlearn.reggie.mapper.CategoryMapper;
import com.tzxlearn.reggie.service.DishService;
import com.tzxlearn.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 20357
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
 * @createDate 2023-02-20 12:03:16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
//    @Resource
//    private CategoryMapper categoryMapper;

    /**
     * 根据id删除分类，在删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //在菜品表中查询当前分类是否已经关联菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);
        if (count > 0) {
            //已经关联菜品，需要自定义一个业务异常
            throw new CustomException("已经关联菜品,无法删除");
        }

        //在套餐表查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(queryWrapper2);
        if (count2 > 0) {
            //已经关联套餐，需要自定义一个业务异常
            throw new CustomException("已经关联套餐,无法删除");
        }

        //都没关联，正常删除
        super.removeById(id);


    }
}




