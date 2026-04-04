package com.example.job_portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ IMPORTANT: frontend URLs allow करो
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://prismatic-rolypoly-f8fb8b.netlify.app"));

        // ✅ headers + methods
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ cookies / JWT support
        config.setAllowCredentials(true);

        // ✅ preflight cache (optional but good)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
