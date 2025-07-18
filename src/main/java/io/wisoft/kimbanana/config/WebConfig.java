package io.wisoft.kimbanana.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads/slide-images").toAbsolutePath().toString();
        registry.addResourceHandler("/slide-images/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
