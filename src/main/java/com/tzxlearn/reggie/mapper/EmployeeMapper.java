package com.tzxlearn.reggie.mapper;

import com.tzxlearn.reggie.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 20357
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2023-02-18 18:47:16
* @Entity com.tzxlearn.reggie.domain.Employee
*/

public interface EmployeeMapper extends BaseMapper<Employee> {

}




