package com.project.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CustomFeignConfig {

    @Bean
    public RequestInterceptor FeignInterceptor() {
        return new FeignInterceptor();
    }

}
