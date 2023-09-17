package com.zhuang.kill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor loginInterceptor(){ return new LoginInterceptor(); }

    @Bean
    public LimitInterceptor limitInterceptor() { return new LimitInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //指定拦截的请求
        String[] path = new String[]{"/**"};
        //String[] path = new String[]{"/killItem/**", "/killOrder/**"};
        //指定不拦截的请求
        String[] excludePath = new String[]{"/", "/user/login", "/user/doLogin", "/css/**", "/img/**", "/js/**"};

        registry.addInterceptor(loginInterceptor()) // 添加拦截器
                .excludePathPatterns(excludePath) // 不拦截的请求
                .addPathPatterns(path); // 添加拦截的请求

//        registry.addInterceptor(limitInterceptor())
//                .addPathPatterns("/killOrder/createOrder/**");
    }
}
