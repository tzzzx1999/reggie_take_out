package com.tzxlearn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tzxlearn.reggie.domain.Employee;
import com.tzxlearn.reggie.service.EmployeeService;
import com.tzxlearn.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 20357
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-02-18 18:47:16
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




