package com.example.simplemvc.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.simplemvc.dto.UsuarioDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUsuarioPS {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutos


    public String generateToken(UsuarioDto usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("nombreUsuario", usuario.getNombreUsuario());
        
        if (usuario.getPersona() != null) {
            claims.put("email", usuario.getPersona().getEmail());
            claims.put("dni", usuario.getPersona().getNumeroDocumento());
        }

        if (usuario.getRoles() != null) {
            claims.put("roles", usuario.getRoles().stream()
                    .map(r -> r.getNombre())
                    .collect(Collectors.toList()));
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuario.getNombreUsuario())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }


    public String extractNombreUsuario(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Map<String, Object> extractUsuarioData(String token) {
        Claims claims = extractAllClaims(token);
        Map<String, Object> data = new HashMap<>();
        data.put("id", claims.get("id"));
        data.put("nombreUsuario", claims.get("nombreUsuario"));
        data.put("email", claims.get("email"));
        data.put("dni", claims.get("dni"));
        data.put("roles", claims.get("roles"));
        return data;
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build();
        return parser.parseClaimsJws(token).getBody();
    }
}
