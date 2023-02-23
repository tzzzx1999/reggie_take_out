package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.User;
import com.tzxlearn.reggie.service.UserService;
import com.tzxlearn.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-02-22 11:22:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




