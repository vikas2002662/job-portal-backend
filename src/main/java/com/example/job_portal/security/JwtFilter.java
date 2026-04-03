package com.example.job_portal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.job_portal.service.CustomUserDetailsService;
import com.example.job_portal.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ 🔥 SKIP AUTH APIs (CRITICAL FIX)
        if (path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // ✅ If no header or wrong format → skip
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // ✅ If token is empty → skip
        if (token.trim().isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // ✅ Extract email
            String email = jwtUtil.extractEmail(token);

            // ✅ Authenticate only if not already authenticated
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = service.loadUserByUsername(email);

                // ✅ OPTIONAL: Validate token properly
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {
            // ✅ Prevent crash
            System.out.println("Invalid JWT Token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
