package com.example.smartmyhome.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    public void addCorsMappings(CorsRegistry registry){
        // 모든 경로에 대해
        registry.addMapping("/**")
                // Origin이 http:localhost:3000 에 대해
                // .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://172.30.1.33:3000","http://localhost:3000","http://172.30.1.43:3000","http://172.30.1.46:3000")
                // GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 허용한다.
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}