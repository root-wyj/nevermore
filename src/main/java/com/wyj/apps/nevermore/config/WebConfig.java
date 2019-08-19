package com.wyj.apps.nevermore.config;

import com.wyj.apps.nevermore.config.interceptor.CorsHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created
 * Author: wyj
 * Date: 2019/8/19
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsHandlerInterceptor getCorsHandlerInterceptor() {
        return new CorsHandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getCorsHandlerInterceptor()).addPathPatterns("/**");
    }
}
