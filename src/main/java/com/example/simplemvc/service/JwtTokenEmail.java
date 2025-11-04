package com.example.simplemvc.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenEmail {

    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final static long EXPIRATION_TIME = 86400000;

    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public String extractEmail(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build();

        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build();

        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}