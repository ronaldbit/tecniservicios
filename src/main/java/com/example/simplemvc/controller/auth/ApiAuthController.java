package com.example.simplemvc.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.service.AuthService;
import com.example.simplemvc.service.UsuarioService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {
  private final UsuarioService usuarioService;
  private final AuthService authService;

  @PostMapping("/login")
  public JwtDto login(@RequestBody LoginUsuarioRequest request) {
    return authService.login(request);
  }

  @PostMapping("/registro")
  public UsuarioDto registro(@RequestBody CrearUsuarioRequest request) {
    return usuarioService.crear(request);
  }
}
