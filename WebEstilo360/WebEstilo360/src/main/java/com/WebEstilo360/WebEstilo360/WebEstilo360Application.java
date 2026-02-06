package com.WebEstilo360.WebEstilo360;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.WebEstilo360.WebEstilo360.interceptor.JwtInterceptor;

@Configuration
@SpringBootApplication
public class WebEstilo360Application {

    private final JwtInterceptor jwtInterceptor;

    public WebEstilo360Application(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebEstilo360Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(jwtInterceptor));
        return restTemplate;
    }
}
