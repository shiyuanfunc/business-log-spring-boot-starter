package com.shiyuanfunc.businesslogstarter.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author SHI YUAN
 * @DATE 2022/3/31 5:56 PM
 * @Version 1.0
 * @Desc
 */
public class OpLogServletRequestWrapperFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            filterChain.doFilter(new OpLogServletRequestWrapper(request), servletResponse);
        }else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
