package com.example.simplemvc.controller;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.request.CrearUsuarioRequest;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.service.AuthService;
import com.example.simplemvc.service.JwtAuthenticationService;
import com.example.simplemvc.service.RecuperarPsService;
import com.example.simplemvc.service.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/auth")
@AllArgsConstructor
public class ApiAuthController {
  @Autowired
  private final UsuarioService usuarioService;

  @Autowired
  private final AuthService authService;

  @Autowired
  private final RecuperarPsService recuperarPsService;

  @Autowired
  private final JwtAuthenticationService jwtAuthenticationService;

  @PostMapping("/login")
  public String login(
      @ModelAttribute LoginUsuarioRequest request, HttpServletResponse response, Model model) {
    try {
      JwtDto jwt = authService.login(request);

      if (jwt == null) {
        model.addAttribute("message", "El login ha fallado");
        return "auth/login";
      }

      model.addAttribute("message", "Login exitoso. JWT: " + jwt.getJwt());

      Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt.getJwt());
      jwtCookie.setHttpOnly(true);
      jwtCookie.setPath("/");
      jwtCookie.setMaxAge(24 * 60 * 60);

      response.addCookie(jwtCookie);

      Usuario usuario = jwtAuthenticationService.fromJwt(jwt.getJwt());

      if (usuario.getEstado() == EstadoEntidad.INACTIVO) {
        model.addAttribute("message", "Usuario inactivo. Contacte al administrador.");
        return "auth/login";
      }


      model.addAttribute("usuario", usuario);

      System.out.println("Usuario logueado: " + usuario.getUsername());

      return "redirect:/auth/login";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "auth/login";
  }

  @PostMapping("/registro")
  public String registro(@ModelAttribute CrearUsuarioRequest request, Model model) {
    UsuarioDto usuario = usuarioService.crear(request);

    model.addAttribute("usuario", usuario);

    return "auth/login";
  }

  @GetMapping("/validar-recuperacion")
  public ResponseEntity<String> validarGenerarToken(
      @RequestParam String email,
      @RequestParam String dni,
      @RequestParam String usuario) {

    try {
      boolean validado = recuperarPsService.validarGenerarToken(email, dni, usuario);
      if (validado) {
        return ResponseEntity.ok("Se ha enviado un enlace de recuperación a tu correo.");
      } else {
        return ResponseEntity.badRequest().body("No se pudo generar el token.");
      }

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body("⚠️ " + e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Error interno del servidor.");
    }
  }


}
