package com.tzxlearn.reggie.common;

/**
 * @description:
 * 自定义业务异常类
 * @author: 田正轩
 * @time: 2023/2/20 15:49
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
