package com.apeck.erp.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * OpenAPI Configuration
 * Configures Swagger/OpenAPI documentation
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "APECK Digital Operations API",
        version = "1.0.0",
        description = "Phase 1: Cash Management Module - REST API for cash requisitions and expenditure retirements",
        contact = @Contact(
            name = "APECK IT Department",
            email = "it@apeckinternational.com"
        )
    ),
    servers = {
        @Server(
            description = "Local Development",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Production",
            url = "https://api.apeckinternational.com"
        )
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}