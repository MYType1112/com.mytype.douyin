package com.mytype.douyin.config;

import com.mytype.douyin.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configurable
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/uploadVideo/**").addResourceLocations("file:D:/work/workspace/douyin/src/main/resources/static/uploadVideo/");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/douyin/user/login", "/douyin/user/register", "/static/**", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //1、添加资源处理器路径 即每次访问静态资源都得添加"/static/",例如localhost:8080/static/j1.jpg
        //若registry.addResourceHandler("/s/**") 则必须访问localhost:8080/s/j1.jpg
        registry.addResourceHandler("/static/uploadVideo/**")
                //2、添加了资源处理器路径后对应的映射资源路径
                .addResourceLocations("classpath:/static/uploadVideo/");
    }
}
