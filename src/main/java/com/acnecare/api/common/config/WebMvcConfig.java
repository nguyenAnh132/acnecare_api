package com.acnecare.api.common.config;

import com.acnecare.api.common.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Lấy đường dẫn gốc của project (ví dụ: C:/Users/Trinh/Project)
        Path rootPath = Paths.get(".").toAbsolutePath().normalize();

        // 2. Tạo đường dẫn đến thư mục uploads/messages
        // file:C:/Users/Trinh/Project/uploads/messages/
        String messageUploadPath = "file:" + rootPath.resolve("uploads").resolve("messages").toString() + "/";

        // 3. Map URL /files/messages/** vào thư mục vật lý
        registry.addResourceHandler("/files/messages/**")
                .addResourceLocations(messageUploadPath);

        // 4. (Tùy chọn) Map cho các thư mục khác nếu bạn có
        registry.addResourceHandler("/files/products/**")
                .addResourceLocations("file:" + rootPath.resolve("uploads").resolve("products").toString() + "/");

        // In ra Console để bạn kiểm tra xem đường dẫn có đúng folder của bạn không
        System.out.println("🚀 WebMvcConfig - Message Path: " + messageUploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}