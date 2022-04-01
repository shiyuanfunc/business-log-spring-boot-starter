package com.shiyuanfunc.businesslogstarter;

import com.alibaba.fastjson.JSON;
import com.shiyuanfunc.businesslogstarter.filter.OpLogServletRequestWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @Author SHI YUAN
 * @DATE 2022/3/31 5:13 PM
 * @Version 1.0
 * @Desc
 */
@Aspect
public class ControllerInterceptorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptorHandler.class);

    @Pointcut(value = "!@annotation(com.shiyuanfunc.businesslogstarter.annotation.NoOpLog) && (@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping))")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object execute(ProceedingJoinPoint proceedingJoinPoint){
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                javax.servlet.http.HttpServletRequest request = servletRequestAttributes.getRequest();
                String requestUrl = request.getRequestURI();
                String paramMap = JSON.toJSONString(request.getParameterMap());
                String paramBody = "";
                if (OpLogServletRequestWrapper.isRequestBody(request)){
                    paramBody = this.readInputStream(request.getInputStream());
                }
                long startTime = System.currentTimeMillis();
                Object proceed = proceedingJoinPoint.proceed();
                long costTime = System.currentTimeMillis() - startTime;
                logger.info("[ControllerInterceptorHandler] request url:{}, costTime:{} | paramsMap:{} | paramsBody:{} | result:{}",
                        requestUrl, costTime, paramMap, paramBody, JSON.toJSONString(proceed));
                return proceed;
            }
            return proceedingJoinPoint.proceed();
        }catch (Throwable exception){

        }
        return null;
    }

    private String readInputStream(InputStream inputStream){
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }catch (Exception ex){

        }
        return "";
    }
}
