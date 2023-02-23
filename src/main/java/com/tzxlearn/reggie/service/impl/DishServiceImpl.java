package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.Category;
import com.tzxlearn.reggie.domain.Dish;
import com.tzxlearn.reggie.domain.DishFlavor;
import com.tzxlearn.reggie.dto.DishDto;
import com.tzxlearn.reggie.service.CategoryService;
import com.tzxlearn.reggie.service.DishFlavorService;
import com.tzxlearn.reggie.service.DishService;
import com.tzxlearn.reggie.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 20357
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-02-20 15:16:56
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品的基本信息保存到dish表,dishDto是dish的子类
        this.save(dishDto);
        //保存口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }
    }

    @Override
    public DishDto querySingleDishDto(Long id) {
        //条件查询菜品口味list
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        //查询菜品
        Dish dishById = this.getById(id);

        //查询菜品分类(根据dish表中的菜品分类id)
        Long categoryId = dishById.getCategoryId();
        Category categoryById = categoryService.getById(categoryId);
        String categoryByIdName = categoryById.getName();

        //将上面三种查询赋给DishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dishById, dishDto);
        dishDto.setFlavors(list);
        dishDto.setCategoryName(categoryByIdName);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);

        //清理当前菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加修改的口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }


    }

    @Override
    @Transactional
    public void delDishAndFlavor(List<Long> idList) {
        //删除菜品信息
        this.removeByIds(idList);
        //删除口味信息
        LambdaUpdateWrapper<DishFlavor> queryWrapper = new LambdaUpdateWrapper<>();
        for (Long aLong : idList) {
            queryWrapper.eq(DishFlavor::getDishId, aLong);
        }
        dishFlavorService.remove(queryWrapper);
    }
}




