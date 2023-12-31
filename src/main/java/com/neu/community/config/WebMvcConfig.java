package com.neu.community.config;

import com.neu.community.controller.interceptor.LoginRequiredInterceptor;
import com.neu.community.controller.interceptor.LoginTicketInterceptor;
import com.neu.community.controller.interceptor.MessageInterceptor;
import com.neu.community.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

//    @Autowired
//    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*/*.css", "/**/*/*.js", "/**/*/*.png", "/**/*.jpg", "/**/*.jpeg");

//        registry.addInterceptor(loginRequiredInterceptor)
//                .excludePathPatterns("/**/*/*.css", "/**/*/*.js", "/**/*/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*/*.css", "/**/*/*.js", "/**/*/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
