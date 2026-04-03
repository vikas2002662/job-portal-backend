package com.example.job_portal.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // ✅ 32+ character secret key (VERY IMPORTANT)
    private static final String SECRET = "mySuperSecureJwtSecretKey1234567890!";

    // ✅ Generate secure key object
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // ✅ Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract email (subject)
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // ✅ Extract all claims
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Check if token expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // ✅ Validate token
    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
}
