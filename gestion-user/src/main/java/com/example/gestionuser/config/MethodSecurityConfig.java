package com.example.gestionuser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity  // 👈 THIS enables @PreAuthorize and friends
public class MethodSecurityConfig {
}
