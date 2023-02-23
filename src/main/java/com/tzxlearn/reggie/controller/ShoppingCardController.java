package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tzxlearn.reggie.common.BaseContext;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.ShoppingCart;
import com.tzxlearn.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: tzx
 * @time: 2023/2/22 18:28
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCardController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加到购物车
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id，指定是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询是否已经有一条数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            //添加到购物车是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if (cart != null) {
            //加入相同菜品两次不需要插入两条数据，需要将已有数据数量+1
            Integer number = cart.getNumber();
            cart.setNumber(number + 1);
            shoppingCartService.updateById(cart);
        } else {
            //不存在，将从前端接受的ShoppingCart添加到购物车中，数量默认是1
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cart = shoppingCart;

        }
        return R.success(cart);
    }

    /**
     * 查询购物车所有
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> listR() {
        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        //查询
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 删除单个菜品
     */
    @PostMapping("/sub")
    public R<String> deleteSingle(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id，指定是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询是否已经有一条数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {
            //减少的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            //减少的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //拿到查询后的结果是单个菜品还是套餐
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        if (cart.getNumber() != 1) {
            //数量大于一，-1
            Integer number = cart.getNumber();
            cart.setNumber(number - 1);
            shoppingCartService.updateById(cart);
        } else {
            //等于1
            shoppingCartService.removeById(cart.getId());
        }
        return R.success("移除成功");
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("clean")
    public R<String> clean() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(queryWrapper);
        return R.success("成功");
    }


}
