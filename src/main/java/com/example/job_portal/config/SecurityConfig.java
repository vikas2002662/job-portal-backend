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
                .cors(cors -> {
                }) // ✅ uses CorsConfig

                .authorizeHttpRequests(auth -> auth

                        // ✅ VERY IMPORTANT (CORS preflight fix)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔓 Public APIs
                        .requestMatchers("/auth/**").permitAll()

                        // 🔌 WebSocket
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

                        // 👔 Employer
                        .requestMatchers("/applications/employer").hasRole("EMPLOYER")

                        // 📊 Dashboard
                        .requestMatchers("/dashboard/employer").hasRole("EMPLOYER")
                        .requestMatchers("/dashboard/admin").hasRole("ADMIN")

                        // 🌍 Public jobs
                        .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()

                        // 💬 Chat
                        .requestMatchers("/chat/**").authenticated()

                        // 🔐 बाकी सब secure
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
