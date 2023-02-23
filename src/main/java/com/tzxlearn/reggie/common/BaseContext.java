package com.tzxlearn.reggie.common;

/**
 * @description: 基于ThreadLocal用于保存和获取当前登录用户的id
 * @author: 田正轩
 * @time: 2023/2/20 11:17
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void throwThreadLocal(){
        threadLocal.remove();
    }
}
