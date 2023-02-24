package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.Dish;
import com.tzxlearn.reggie.domain.Setmeal;
import com.tzxlearn.reggie.domain.SetmealDish;
import com.tzxlearn.reggie.dto.SetmealDto;
import com.tzxlearn.reggie.service.CategoryService;
import com.tzxlearn.reggie.service.SetmealDishService;
import com.tzxlearn.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 套餐管理
 * @author: 田正轩
 * @time: 2023/2/21 16:55
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CacheManager cacheManager;

    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmeal",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 展示套餐列表
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> list(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);
        //查询
        setmealService.page(setmealPage, queryWrapper);

        //拷贝
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDto> collect = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(collect);

        return R.success(setmealDtoPage);

    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmeal",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 停售起售更改
     */
    @PostMapping("/status/{status}")
    @Transactional
    @CacheEvict(value = "setmeal",allEntries = true)
    public R<String> updateStatus(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {

        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids).set(Setmeal::getStatus, status);

        boolean update = setmealService.update(updateWrapper);
        return R.success("修改成功");

    }

    /**
     * 在客户端点击左侧套餐分类，在右侧显示套餐
     */
    @GetMapping("/list")
    @Cacheable(value = "setmeal", key = "#categoryId+'_'+#status")
    public R<List<Setmeal>> setmealList(Long categoryId, Integer status) {
        //条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, status);
        //查询
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }


}
