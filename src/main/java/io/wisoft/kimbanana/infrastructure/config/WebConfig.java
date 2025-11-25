package io.wisoft.kimbanana.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String uploadPath = "file:" + System.getProperty("user.dir") + "/uploads/slide-images/";
        String thumbnailPath = "file:" + System.getProperty("user.dir") + "/uploads/presentation-thumbnails/";

        registry.addResourceHandler("/slide-images/**")
                .addResourceLocations(uploadPath);
        registry.addResourceHandler("/presentation-thumbnails/**")
                .addResourceLocations(thumbnailPath);

        registry.addResourceHandler("/kimbanana/app/openapi/**")
                .addResourceLocations("classpath:/openapi/")
                .setCachePeriod(0);
    }
}