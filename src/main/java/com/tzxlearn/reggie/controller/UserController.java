package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.User;
import com.tzxlearn.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @description:
 * @author: 田正轩
 * @time: 2023/2/22 11:40
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 验证码获取，没有买服务，直接放行
     *
     * @param user
     * @return
     */


    /**
     * 登录验证，直接放行
     *
     * @param userMap
     * @return
     */
    @PostMapping("/login")
    public R<User> sendMsg(@RequestBody Map userMap, HttpSession session) {

        System.out.println(userMap.toString());
        String phone = (String) userMap.get("phone");
        //判断是否为新用户，是新用户就自动完成注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);

        if (user == null) {
            //新用户
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }
        session.setAttribute("user", user.getId());

        return R.success(user);
    }
}
