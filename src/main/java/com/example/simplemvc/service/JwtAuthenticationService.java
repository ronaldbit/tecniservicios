package com.example.simplemvc.service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public final class JwtAuthenticationService {
  @Value("${security.jwt.secret}")
  private String secretKey;
  private Key key;

  private final UsuarioMapper usuarioMapper;
  private final ObjectMapper objectMapper;

  public String toJwt(Usuario usuario) {
    Objects.requireNonNull(usuario, "El usuario no puede ser nulo");
    UsuarioDto usuarioDto = usuarioMapper.toDto(usuario);

    String jwt = Jwts.builder()
        .claims(
            new HashMap<String, Object>() {
              {
                put("usuario", objectMapper.convertValue(usuarioDto, new TypeReference<Map<String, Object>>() {
                }));
              }
            })
        .subject(String.valueOf(usuario.getId()))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();

    return jwt;
  }

  public boolean isValid(String jwt) {
    try {
      getClaims(jwt);
      return true;
    } catch (Exception e) {
      log.error("Error while validating JWT", e);

      return false;
    }
  }

  public Usuario fromJwt(String jwt) {
    try {
      return getClaim(
          jwt, claims -> usuarioMapper.toDomain(objectMapper.convertValue(claims.get("usuario"), UsuarioDto.class)));
    } catch (Exception e) {
      log.error("Error al obtener el usuario del JWT");
      return null;
    }
  }

  private Claims getClaims(String jwt) {
    SecretKey secretKey = new SecretKeySpec(getKey().getEncoded(), getKey().getAlgorithm());

    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
  }

  public <T> T getClaim(String jwt, Function<Claims, T> resolver) {
    Claims claims = getClaims(jwt);

    return resolver.apply(claims);
  }

  private Key getKey() {
    if (key == null) {
      key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    return key;
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}