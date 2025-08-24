package com.poly.util;



import java.io.File;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	    	

        String absolutePath = new File("").getAbsolutePath() + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath);
        
        
        // Nếu lưu file trong project root: /{project-root}/uploads/
        String projectRoot = new File("").getAbsolutePath();
        String productPath = projectRoot + "/uploads/products/";
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + productPath);
  
    }
    
    
 
  
}

