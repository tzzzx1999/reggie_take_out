package com.tzxlearn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tzxlearn.reggie.common.BaseContext;
import com.tzxlearn.reggie.common.R;
import com.tzxlearn.reggie.domain.Employee;
import com.tzxlearn.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: 田正轩
 * @time: 2023/2/18 18:50
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;


    /**
     * @param request  ：获取session对象
     * @param employee ：接收前端输入的用户名密码
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /*
         * 1.将页面提交的密码进行md5处理
         * 2.检查数据库中是否有该用户
         * 3.没有返回登陆失败
         * 4.有该用户则查询密码，不一致返回登陆失败
         * 5.查看员工状态，如果为禁用状态，则返回员工已禁用结果
         * 6.登陆成功，将员工id存入Session并返回登陆成功结果
         *
         * */
        //1
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee employeeFromDB = employeeService.getOne(queryWrapper);
        //3
        if (employeeFromDB == null) {
            return R.error("登陆失败，用户名不存在。。");
        }
        //4
        if (!employeeFromDB.getPassword().equals(password)) {
            return R.error("登陆失败，密码错误。。");
        }
        //5.禁用为0、正常为1
        if (employeeFromDB.getStatus() == 0) {
            return R.error("登陆失败，用户已被禁用。。");
        }
        //6
        HttpSession session = request.getSession();
        session.setAttribute("employee", employeeFromDB.getId());
        return R.success(employeeFromDB);
    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功。。。");

    }

    /**
     * 员工新增
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("新增员工信息：" + employee);
        //设置初始密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        boolean save = employeeService.save(employee);
        return R.success("添加成功");
    }


    /**
     * 员工列表展示
     *
     * @param page
     * @param pageSize
     * @return
     */
    //http://localhost:8080/employee/page?page=1&pageSize=10
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Employee> employeePage = new Page<>();
        employeePage.setCurrent(page);
        employeePage.setSize(pageSize);
        //使用条件构造器进行查询
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(employeePage, lambdaQueryWrapper);
        return R.success(employeePage);
    }


    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {

        Long updateEmployee = (Long) request.getSession().getAttribute("employee");
        System.out.println("账号id为: " + employee.getId());
        System.out.println("目标帐号状态为：" + employee.getStatus());
        //记录修改时间和操作人员
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(updateEmployee);
        //改变帐号状态
        boolean b = employeeService.updateById(employee);
        if (!b) {
            return R.success("改变状态失败。。。");
        }
        return R.success("信息修改成功。。。");

    }

    /**
     * 查询单个员工信息
     *
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getSingleEmployee(@PathVariable("id") Long id) {
        Employee byId = employeeService.getById(id);
        if (byId == null) {
            return R.error("错误");
        }
        return R.success(byId);
    }
}
