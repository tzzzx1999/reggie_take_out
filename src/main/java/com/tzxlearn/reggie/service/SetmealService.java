package com.tzxlearn.reggie.service;

import com.tzxlearn.reggie.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tzxlearn.reggie.dto.SetmealDto;

import java.util.List;

/**
* @author 20357
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-02-21 16:47:56
*/
public interface SetmealService extends IService<Setmeal> {

    //新增套餐并保存和菜品的关联关系
    void saveWithDish(SetmealDto setmealDto);

    //删除套餐
    void removeWithDish(List<Long> ids);
}
