package com.apeck.erp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * File Storage Configuration
 * Configures file upload directory and settings
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.upload")
public class FileStorageConfig {
    
    private String dir = "./uploads";
    
    /**
     * Get the upload directory path
     */
    public String getUploadDir() {
        return dir;
    }
}