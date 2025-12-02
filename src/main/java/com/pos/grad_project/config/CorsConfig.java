package com.pos.grad_project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://192.168.1.166:3000",
                        "http://localhost:3000"
                )
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // مهم لو هترجع header
                .allowCredentials(false); // ❗ بقيت false لأن مالناش Cookies
    }
}


