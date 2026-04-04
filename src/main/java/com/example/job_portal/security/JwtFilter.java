package com.example.job_portal.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.job_portal.util.JwtUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        // ✅ Allow preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // ✅ Skip only auth APIs
        if (path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(auth);

                System.out.println("✅ Authenticated: " + email + " | ROLE_" + role);
            }

        } catch (Exception e) {
            System.out.println("❌ JWT Error: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}