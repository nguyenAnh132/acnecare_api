package com.acnecare.api.common.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Component
@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class StorageProperties {
    String uploadDir;
    String baseUrl;
    String allowedTypes;
    long maxFileSize;            
    long maxUserSize;            
    long maxFacescanUserSize;    
}
