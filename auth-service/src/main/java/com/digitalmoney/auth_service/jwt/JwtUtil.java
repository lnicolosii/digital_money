package com.digitalmoney.auth_service.jwt;

import com.digitalmoney.auth_service.dto.User.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtil {

    // Usá una clave de mínimo 32 caracteres (256 bits), codificada en Base64
    private static final String SECRET_KEY_STRING = "XxYyZz1234567890AbCdEfGhIjKlMnOpQ"; // por ejemplo
    private final SecretKey SECRET_KEY;

    public JwtUtil() {
        // Convertimos la string a bytes y luego a SecretKey
        byte[] keyBytes = SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8);
        this.SECRET_KEY = Keys.hmacShaKeyFor(keyBytes);

    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Long userId, UserDto userData) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userData.getEmail());
        claims.put("first_name", userData.getFirstName());
        claims.put("last_name", userData.getLastName());
        claims.put("dni", userData.getDni());
        claims.put("phone", userData.getPhone());
        return createToken(claims, userId.toString());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 día
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDto userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUserId().toString()) && !isTokenExpired(token));
    }
}
