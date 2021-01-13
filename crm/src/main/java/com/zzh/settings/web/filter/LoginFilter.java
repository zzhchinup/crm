package com.zzh.settings.web.filter;

import com.zzh.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 防止死循环，要放行login页，和login请求。
        String path = httpRequest.getServletPath();
        if("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            chain.doFilter(request,response);
        } else {

            User user = (User) httpRequest.getSession().getAttribute("user");
            if (user != null) {
                // 说明有user这个东西，登录过。
                chain.doFilter(request,response);
            } else {
                // 重定向到登录页

                // 重定向是使用的传统路径，
                // 前面要加/项目名称
                // 最好是动态地获取项目的名称

                // ${pageContext.request.contextPath}

                // 为什么是重定向
                // 转发的路径是不改变的
                // 我们应该在为用户跳转到登录页的同时，地址栏也应该变。
                httpResponse.sendRedirect(httpRequest.getContextPath()+"/login.jsp");
            }
        }
    }
}
