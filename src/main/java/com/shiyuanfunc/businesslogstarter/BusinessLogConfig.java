package com.shiyuanfunc.businesslogstarter;

import com.shiyuanfunc.businesslogstarter.filter.OpLogServletRequestWrapperFilter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author SHI YUAN
 * @DATE 2022/3/31 5:10 PM
 * @Version 1.0
 * @Desc
 */
@Configuration
public class BusinessLogConfig {

    @Bean
    @ConditionalOnClass(value = {HttpServletRequest.class, ProceedingJoinPoint.class})
    public ControllerInterceptorHandler controllerInterceptorHandler(){
        return new ControllerInterceptorHandler();
    }

    @Bean(name = "opLogfilterRegistrationBean")
    @ConditionalOnClass(value = {HttpServletRequest.class})
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new OpLogServletRequestWrapperFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("OpLogRequestFilter");
        return registrationBean;
    }
}
