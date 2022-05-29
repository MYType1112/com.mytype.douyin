package com.mytype.douyin.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class HttpAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    // 切点，用于匹配所要执行功能的方法
    @Pointcut("execution(* com.mytype.douyin.controller.*.*(..))")
    public void log(){
    }
    // @Before该注解下的方法，要在执行与切点匹配的方法之前执行，匹配模式有log() 决定
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // url
        logger.info("url={}.", request.getRequestURL());
        // method
        logger.info("method={}.", request.getMethod());
        // ip
        logger.info("ip={}.", request.getRemoteAddr());
        // 类方法
        logger.info("class_method={}.", joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        // 参数名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
                logger.info("args={}.", params);
            }
        }
        // 参数
//        Object[] args = joinPoint.getArgs();
//        for (Object arg:args) {
//            logger.info("args={}.", arg);
//        }
    }

    // @After该注解下的方法，要在执行与切点匹配的方法之后执行，匹配模式有log() 决定
    @After("log()")
    public void doAfter() {
        logger.info("doAfter");
    }

    // @After该注解下的方法，要在执行与切点匹配的方法之后执行，该注解能够获取方法执行完成之后返回值，匹配模式有log() 决定
    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturn(Object object) {
        logger.info("returning={}.", object);
    }
}
