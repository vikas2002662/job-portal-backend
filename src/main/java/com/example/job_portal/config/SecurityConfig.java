package com.example.job_portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.*;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.job_portal.security.JwtFilter;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth

                        // ✅ CORS preflight allow
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ public APIs
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()

                        // ✅ JOB SEEKER
                        .requestMatchers(HttpMethod.POST, "/resume/upload").hasAuthority("ROLE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/resume/download").hasAuthority("ROLE_JOB_SEEKER")
                        .requestMatchers("/applications/apply/**").hasAuthority("ROLE_JOB_SEEKER")

                        // ✅ EMPLOYER
                        .requestMatchers(HttpMethod.POST, "/jobs").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/jobs/**").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers(HttpMethod.DELETE, "/jobs/**").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers("/applications/employer").hasAuthority("ROLE_EMPLOYER")
                        .requestMatchers("/dashboard/employer").hasAuthority("ROLE_EMPLOYER")

                        // ✅ ADMIN
                        .requestMatchers("/dashboard/admin").hasAuthority("ROLE_ADMIN")

                        // ✅ public jobs
                        .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()

                        // ✅ बाकी सब authenticated
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ FINAL CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "https://prismatic-rolypoly-f8fb8b.netlify.app"));

        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"));

        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept"));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}