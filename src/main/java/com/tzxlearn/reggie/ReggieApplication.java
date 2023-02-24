package com.tzxlearn.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description:
 * @author: 田正轩
 * @time: 2023/2/17 10:55
 */
@EnableCaching//开启注解缓存
@Slf4j//直接使用log变量
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.tzxlearn.reggie.mapper")
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class);
        log.info("项目启动成功。。。");
    }
}
