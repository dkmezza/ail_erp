package com.apeck.erp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * JWT Configuration Properties
 * Binds jwt properties from application.properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secret;
    private long expirationMs; // using primitive long (avoid null)
}
