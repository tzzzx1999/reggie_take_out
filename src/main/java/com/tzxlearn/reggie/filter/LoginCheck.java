package com.tzxlearn.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.tzxlearn.reggie.common.BaseContext;
import com.tzxlearn.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 拦截未登录访问的过滤器
 * @author: 田正轩
 * @time: 2023/2/18 20:53
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheck implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取请求url
        String requestURI = request.getRequestURI();
        log.info(requestURI);
        //定义不需要拦截的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/senMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        //判断本次请求是否需要处理
        boolean checkResult = check(urls, requestURI);

        //不需要处理,直接放行
        if (checkResult) {
            log.info("本次请求不需要处理" + requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        //需要处理，判断后台管理系统用户是否登录
        Object employee = request.getSession().getAttribute("employee");
        if (employee != null) {
            //设置创建人到当前请求的线程中
            Long employeeIDFromSession = (Long) employee;
            BaseContext.setCurrentId(employeeIDFromSession);
            //已登录，放行
            log.info("用户已登录" + employee);

            filterChain.doFilter(request, response);
            return;
        }

        //需要处理，判断客户端用户是否登录
        Object user = request.getSession().getAttribute("user");
        if (user != null) {
            Long userIDFromSession = (Long) user;
            BaseContext.setCurrentId(userIDFromSession);
            //已登录，放行
            log.info("用户已登录" + user);

            filterChain.doFilter(request, response);
            return;
        }

        //通过response对象来向前端输出未登录数据
        PrintWriter writer = response.getWriter();
        //这里是把R对象转成JSON再通过response对象对象传给前端
        writer.write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配方法
     *
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls, String requestURL) {
        for (String url : urls) {
            boolean match = PATH_MATHER.match(url, requestURL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
