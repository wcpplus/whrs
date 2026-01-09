package org.farm2.main.filter;

import jakarta.servlet.*;
import org.farm2.base.web.Farm2WebVisitListenner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听访问量
 */
@Component
@Order(100)
public class FilterVisitListenner implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 监听服务请求
        Farm2WebVisitListenner.visit(servletRequest);
        // 继续处理请求链
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
