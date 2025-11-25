package com.example.simplemvc.service;

import com.example.simplemvc.dto.UsuarioDto;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecuperarPsService {

  private final UsuarioRepository usuarioRepository;
  private final JwtUsuarioPS jwtUsuarioPS;
  private final UsuarioMapper usuarioMapper;
  private final ServicioCorreo servicioCorreo;

  private final PasswordEncoder passwordEncoder;

  public boolean validarGenerarToken(String email, String dni, String username) {
    log.info("Intentando recuperar contraseña para el usuario: {}", username);

    Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(username);

    if (usuarioOpt.isEmpty()) {
      log.error("Usuario no encontrado con nombre de usuario: {}", username);
      throw new IllegalArgumentException("Usuario no encontrado");
    }

    

    Usuario usuario = usuarioOpt.get();

    if (usuario.getPersona() == null) {
      log.error("El usuario {} no tiene datos de persona asociados", username);
      throw new IllegalArgumentException("Usuario sin información personal");
    }

    boolean datosCorrectos = usuario.getPersona().getEmail().equalsIgnoreCase(email) &&
        usuario.getPersona().getNumeroDocumento().equalsIgnoreCase(dni);

    if (!datosCorrectos) {
      log.error("Datos de verificación incorrectos para el usuario: {}", username);
      throw new IllegalArgumentException("Datos de verificación incorrectos");
    }
    if (usuario.getPersona().getEmailVerificado() == false) {
      System.out.println("Estado de la persona: " + usuario.getPersona().getEmailVerificado());
      log.error("El usuario {} no ha verificado su correo electrónico", username);
      throw new IllegalArgumentException("Usuario no verificado");
    }
    UsuarioDto usuarioDto = usuarioMapper.toDto(usuario);
    String token = jwtUsuarioPS.generateToken(usuarioDto);
    usuario.setJwtPSecret(token);
    usuarioRepository.save(usuario);
    servicioCorreo.enviarOlvidoCorreo(usuario.getPersona().getEmail(), token);
    log.info("Token generado y guardado correctamente para el usuario: {}", username);
    return true;
  }

  public boolean validarToken(String token) {
    log.info("Validando token recibido...");
    if (!jwtUsuarioPS.validateToken(token)) {
      log.error("Token inválido o expirado");
      return false;
    }

    return usuarioRepository.findByJwtPSecret(token).isPresent();
  }

  public boolean cambiarPassword(String token, String nuevaPassword) {
    log.info("Intentando cambiar contraseña mediante token de recuperación...");

    if (!jwtUsuarioPS.validateToken(token)) {
      log.error("Token inválido o expirado");
      throw new IllegalArgumentException("Token inválido o expirado");
    }
    Usuario usuario = usuarioRepository.findByJwtPSecret(token)
        .orElseThrow(() -> new IllegalArgumentException("Token no asociado a ningún usuario"));

    usuario.setPassword(passwordEncoder.encode(nuevaPassword));
    usuario.setJwtPSecret(null);
    usuarioRepository.save(usuario);

    log.info("Contraseña cambiada correctamente para usuario: {}", usuario.getNombreUsuario());
    return true;
  }

}
