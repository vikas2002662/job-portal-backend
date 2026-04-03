package com.example.job_portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.job_portal.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})

            .authorizeHttpRequests(auth -> auth

                // 🔓 Public APIs
                .requestMatchers("/auth/**").permitAll()

                // ✅ KEY FIX: WebSocket endpoints allow karo
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/ws/info").permitAll()

                // 🧾 Resume APIs
                .requestMatchers(HttpMethod.POST, "/resume/upload").hasRole("JOB_SEEKER")
                .requestMatchers(HttpMethod.GET, "/resume/download").hasRole("JOB_SEEKER")
                .requestMatchers(HttpMethod.GET, "/resume/view/**").permitAll()

                // 💼 Job APIs
                .requestMatchers(HttpMethod.POST, "/jobs").hasRole("EMPLOYER")
                .requestMatchers(HttpMethod.PUT, "/jobs/**").hasRole("EMPLOYER")
                .requestMatchers(HttpMethod.DELETE, "/jobs/**").hasRole("EMPLOYER")

                // 📩 Apply Job
                .requestMatchers("/applications/apply/**").hasRole("JOB_SEEKER")

                // 👔 Employer view applications
                .requestMatchers("/applications/employer").hasRole("EMPLOYER")

                // 📊 Employer Dashboard
                .requestMatchers("/dashboard/employer").hasRole("EMPLOYER")

                // 🛡️ Admin Dashboard
                .requestMatchers("/dashboard/admin").hasRole("ADMIN")

                // 🌍 Public GET jobs
                .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()

                // 💬 Chat REST endpoints
                .requestMatchers("/chat/**").authenticated()

                // 🔐 Everything else
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}