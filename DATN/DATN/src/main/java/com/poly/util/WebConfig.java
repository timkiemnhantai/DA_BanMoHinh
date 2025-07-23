package com.poly.util;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:///C:\\Users\\Admin\\Documents\\workspace-spring-tool-suite-4-4.27.0.RELEASE\\DATN\\src\\main\\resources\\static\\avatar"); // đúng path thư mục chứa ảnh
    }
}
