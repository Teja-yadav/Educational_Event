package com.edutech.educationalresourcedistributionsystem.jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.util.Date;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
@Component
public class JwtUtil {
   private final SecretKey key =
          Keys.hmacShaKeyFor(
            "edutecheducationalresourcemanagementsecurekey12345"
            .getBytes()
           );
   public String generateToken(String username, String role) {
       return Jwts.builder()
              .setSubject(username)
              .claim("role", role)
              .setIssuedAt(new Date())
              .setExpiration(
                  new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
               )
              .signWith(key)
              .compact();
   }
   public String extractUsername(String token) {
       return Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody()
              .getSubject();
   }
   public String extractRole(String token) {
       return Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody()
              .get("role", String.class);
   }
   public boolean validateToken(String token) {
       return extractUsername(token) != null;
   }
}