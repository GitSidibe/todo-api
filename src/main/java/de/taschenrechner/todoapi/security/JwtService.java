package de.taschenrechner.todoapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // In Produktion: aus application.properties laden!
    private static final String SECRET = "meinSehrGeheimerSchluesselFuerJwtBitteMindestens32Zeichen";
    private static final long EXPIRATION_MS = 1000 * 60 * 60; // 1 Stunde

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token generieren
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    // Username aus Token extrahieren
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token validieren
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}