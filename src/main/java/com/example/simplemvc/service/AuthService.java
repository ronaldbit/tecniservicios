package com.example.simplemvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.request.LoginUsuarioRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final PasswordEncoder passwordEncoder;
  private final GetCurrentUsuarioService getCurrentUsuarioService;
  private final JwtAuthenticationService jwtAuthenticationService;

  private final UsuarioService usuarioService;

  public JwtDto login(LoginUsuarioRequest request) {
    log.info(
        "Iniciando sesión para el usuario con nombre de usuario: {}", request.getNombreUsuario());

    try {
      getCurrentUsuarioService.get();

      log.info(
          "El usuario con nombre de usuario {} ya ha iniciado sesión.", request.getNombreUsuario());
      throw new IllegalArgumentException("El usuario ya ha iniciado sesión.");
    } catch (Exception ignored) {
    }

    Usuario usuario = usuarioService
        .obtenerEntidadPorNombreUsuario(request.getNombreUsuario())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
      log.error(
          "Error de autenticación: Contraseña inválida para el usuario con nombre de usuario {}.",
          request.getNombreUsuario());

      throw new IllegalArgumentException("Credenciales inválidas");
    }

    log.info(
        "Usuario con nombre de usuario {} autenticado exitosamente.", request.getNombreUsuario());

    String jwt = jwtAuthenticationService.toJwt(usuario);

    return JwtDto.builder().jwt(jwt).build();
  }
}
