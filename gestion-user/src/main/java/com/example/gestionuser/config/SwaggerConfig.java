package com.example.gestionuser.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new Info()
            .title("Gestion User API")
            .version("1.0.0")
            .description("Documentation for JWT-secured user module"));
    }
}
