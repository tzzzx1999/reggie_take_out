package com.tzxlearn.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @description: 全局异常捕获，通过AOP
 * @author: 田正轩
 * @time: 2023/2/19 11:12
 */
//拦截加了RestController注解和Controller注解的controller类
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法，处理SQLIntegrityConstraintViolationException异常
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        if (ex.getMessage().contains("Duplicate entry")) {

            //违反了账号不能重复
            String[] s = ex.getMessage().split(" ");
            return R.error(s[2] + "已存在");
        }
        log.info(ex.getMessage());
        return R.error("未知错误");
    }


    /**
     * 异常处理方法，处理删除分类CustomException异常
     *
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {

        return R.error(ex.getMessage());
    }

}
