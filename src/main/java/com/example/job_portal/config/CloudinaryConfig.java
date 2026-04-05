package com.example.job_portal.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", "dwet2jn7k",
                "api_key", "531934392939849",
                "api_secret", "6elZJLD8E5ZujaSrGcAcN5L6sgo"));
    }
}