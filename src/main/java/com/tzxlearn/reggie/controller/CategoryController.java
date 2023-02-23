package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.Category;
import com.tzxlearn.reggie.domain.Employee;
import com.tzxlearn.reggie.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 菜品分类控制层
 * @author: 田正轩
 * @time: 2023/2/20 12:08
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功。。。");
    }

    /**
     * 展示所有分类
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> list(Integer page, Integer pageSize) {
        Page<Category> categoryPage = new Page<>();
        categoryPage.setSize(pageSize);
        categoryPage.setCurrent(page);
        LambdaQueryWrapper<Category> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper.orderByAsc(Category::getSort);
        return R.success(categoryService.page(categoryPage, categoryQueryWrapper));
    }


    /**
     * 添加菜品时展示菜品分类
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> showInAdd(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 根据id删除分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {

        categoryService.remove(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {

        boolean update = categoryService.updateById(category);
        if (update) {
            return R.success("更新成功。。");
        }
        return R.success("更新失败");
    }

}
