package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.Category;
import com.tzxlearn.reggie.domain.Dish;
import com.tzxlearn.reggie.domain.DishFlavor;
import com.tzxlearn.reggie.dto.DishDto;
import com.tzxlearn.reggie.service.CategoryService;
import com.tzxlearn.reggie.service.DishFlavorService;
import com.tzxlearn.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 田正轩
 * @time: 2023/2/20 19:53
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 添加菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        System.out.println(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        System.out.println(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 回显菜品
     *
     * @param
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> change(@PathVariable Long id) {
        DishDto dishDto = dishService.querySingleDishDto(id);
        return R.success(dishDto);
    }

    /**
     * 删除菜品
     */
    @DeleteMapping
    public R<String> delete(String ids) {
        String[] split = ids.split(",");
        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            Long aLong = Long.valueOf(split[i]);
            idList.add(aLong);
        }

        dishService.delDishAndFlavor(idList);

        return R.success("删除成功");
    }

    /**
     * 停售起售更改
     */
    @PostMapping("/status/{status}")
    @Transactional
    public R<String> updateStatus(@PathVariable("status") Integer status, String ids) {
        String[] split = ids.split(",");
        List<Long> idList = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            Long aLong = Long.valueOf(split[i]);
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Dish::getId, aLong).set(Dish::getStatus, status);
            dishService.update(updateWrapper);
        }
        return R.success("修改成功");

    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R<Page> list(Integer page, Integer pageSize, String name) {
        //分页对象
        Page<DishDto> dtoPageInfo = new Page<>(page, pageSize);
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        //条件查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name)
                .orderByDesc(Dish::getUpdateTime);
        Page<Dish> pageResult = dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageResult, dtoPageInfo, "records");

        //根据分类id查询出分类名称，给每个Dto的分类名称赋值，然后将
        List<Dish> dishList = pageInfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();


        for (Dish dish : dishList) {
            //新建dto对象，将dish拷贝到dto
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            //根据id查询分类对象，将分类名称放到dto
            Category categoryById = categoryService.getById(categoryId);
            String categoryName = categoryById.getName();
            dishDto.setCategoryName(categoryName);
            dishDtos.add(dishDto);
        }
        dtoPageInfo.setRecords(dishDtos);


        //
        return R.success(dtoPageInfo);

    }

//    /**
//     * 用作套餐管理的菜品菜单
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> listR(Dish dish) {
//        //查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
//                //起售状态
//                .eq(Dish::getStatus,1)
//                .orderByAsc(Dish::getSort)
//                .orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//
//    }

    /**
     * 用作套餐管理的菜品菜单
     */
    @GetMapping("/list")
    public R<List<DishDto>> listR(DishDto dish) {
        //查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId())
                //起售状态
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        //将Dish中的属性复制到DishDto中
        List<DishDto> collect = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            //查询对应菜品的口味信息，赋值给DishDto
            //构建条件
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, dishDto.getId());
            //进行查询和赋值
            List<DishFlavor> list1 = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(list1);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(collect);

    }


}
