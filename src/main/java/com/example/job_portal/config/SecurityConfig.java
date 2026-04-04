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

                        // ✅ OPTIONS allow (CORS fix)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/ws/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/resume/upload").hasAuthority("ROLE_JOB_SEEKER")
                        .requestMatchers(HttpMethod.GET, "/resume/download").hasAuthority("ROLE_JOB_SEEKER")

                        .requestMatchers(HttpMethod.POST, "/jobs").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/jobs/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.DELETE, "/jobs/**").hasRole("EMPLOYER")

                        .requestMatchers("/applications/apply/**").hasRole("JOB_SEEKER")
                        .requestMatchers("/applications/employer").hasRole("EMPLOYER")

                        .requestMatchers("/dashboard/employer").hasRole("EMPLOYER")
                        .requestMatchers("/dashboard/admin").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()

                        .anyRequest().authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ 🔥 FINAL CORS CONFIG
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