package com.acnecare.api.common.config;

import com.acnecare.api.common.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL /files/** → thư mục ./uploads/ trên disk
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + storageProperties.getUploadDir() + "/");
    }
}
