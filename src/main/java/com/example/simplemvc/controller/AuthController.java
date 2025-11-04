package com.example.simplemvc.controller;

import com.example.simplemvc.service.RecuperarPsService;
import com.example.simplemvc.service.SucursalService;
import com.example.simplemvc.service.TipoDocumentoService;
import com.example.simplemvc.service.VerificacionService;

import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  @Autowired
  private final TipoDocumentoService tipoDocumentoService;

  @Autowired
  private final VerificacionService verificacionService;

  @Autowired
  private final RecuperarPsService recuperarPsService;

  @Autowired
  private final SucursalService sucursalService;

  @GetMapping("/registro-persona")
  public String registroPersona(Model model) {
    model.addAttribute("tiposDocumento", tipoDocumentoService.lista());

    return "/auth/registro-persona";
  }

  @GetMapping("/registro")
  public String registroUsuario(Model model) {
    System.err.println("sucursalService.lista(): " + sucursalService.lista().size());
    model.addAttribute("sucursales", sucursalService.lista());

    if (model.getAttribute("persona") == null) {
      return "redirect:/auth/registro-persona";
    }

    return "/auth/registro";
  }

  @GetMapping("/verify")
  public String verificarEmail(@RequestParam String token, Model model) {
    try {
      verificacionService.verificarEmail(token);
      model.addAttribute("titulo", "Verificación Exitosa");
      model.addAttribute("mensaje", "Tu correo electrónico ha sido verificado correctamente.");
      model.addAttribute("tipo", "success");
      return "/auth/verificacion";
    } catch (IllegalArgumentException e) {
      model.addAttribute("titulo", "Error de Verificación");
      model.addAttribute("mensaje", e.getMessage());
      model.addAttribute("tipo", "error");
      return "/auth/verificacion";
    } catch (Exception e) {
      model.addAttribute("titulo", "Error Interno");
      model.addAttribute("mensaje", "Ocurrió un error inesperado.");
      model.addAttribute("tipo", "error");
      return "/auth/verificacion";
    }
  }

  @GetMapping("/reset-password")
  public String resetPassword(@RequestParam String token, Model model) {
    if (!recuperarPsService.validarToken(token)) {
      model.addAttribute("mensajeError", "El enlace de recuperación es inválido o ha expirado.");
      return "/auth/error-token"; 
    }

    model.addAttribute("token", token);
    return "/auth/reset-password";
  }
  @PostMapping("/update-password")
  public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> data) {
    String token = data.get("token");
    String nuevaPassword = data.get("nuevaPassword");
    try {
      recuperarPsService.cambiarPassword(token, nuevaPassword);
      return ResponseEntity.ok("Contraseña actualizada correctamente");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body("Error interno del servidor");
    }
  }
}
