package com.tzxlearn.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @description: 元数据对象处理器
 * @author: 田正轩
 * @time: 2023/2/20 10:46
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        //        //设置时间属性
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateTime(LocalDateTime.now());

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
//        //设置创建人
//        Long employeeIDFromSession = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(employeeIDFromSession);
//        employee.setUpdateUser(employeeIDFromSession);
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());



    }

    //更新自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        System.out.println(BaseContext.getCurrentId()+"=====================");
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }
}
