package com.tzxlearn.reggie.service;

import com.tzxlearn.reggie.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 20357
 * @description 针对表【category(菜品及套餐分类)】的数据库操作Service
 * @createDate 2023-02-20 12:03:16
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
