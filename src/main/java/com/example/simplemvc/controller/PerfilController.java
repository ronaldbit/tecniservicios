package com.example.simplemvc.controller;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.enums.EstadoEntidad;
import com.example.simplemvc.request.CrearUsuarioClienteRequest;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.shared.ClienteSesion;
import com.example.simplemvc.service.AuthService;
import com.example.simplemvc.service.JwtAuthenticationService;
import com.example.simplemvc.service.UsuarioService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.antlr.v4.runtime.atn.SemanticContext.OR;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE) // dejalo pa que dynamic no interfira dskfjha
public class PerfilController {

    private final UsuarioService usuarioService;
    @Autowired
    private final JwtAuthenticationService jwtAuthenticationService;
    @Autowired
    private final AuthService authService;

    // /perfil -> aquí luego pondrás perfil o formulario de registro según sesión
    @GetMapping
    public String perfilInicio(Model model, HttpSession session) { // Eliminé HttpSession, no se usaba

        model.addAttribute("pageTitle", "Mi perfil");

        // Añade el DTO para el formulario de REGISTRO (th:object="${usuarioRequest}")
        if (!model.containsAttribute("usuarioRequest")) {
            model.addAttribute("usuarioRequest", new CrearUsuarioClienteRequest());
        }

        // Añade el DTO para el formulario de LOGIN (th:object="${login}")
        if (!model.containsAttribute("login")) {
            model.addAttribute("login", new LoginUsuarioRequest());
        }

        return "tienda/perfil/index"; // Carga tu plantilla
    }

    // LOGIN CLIENTE (desde menú/header)
    @PostMapping("/login")
    public String loginCliente(
            @RequestParam("identificador") String identificador,
            @RequestParam("password") String password,
            RedirectAttributes ra,
            HttpServletResponse response) { 

        String trimmed = identificador == null ? "" : identificador.trim();
        if (trimmed.isEmpty() || password == null || password.isBlank()) {
            ra.addFlashAttribute("loginError", "Ingresa tu DNI o correo y la contraseña.");
            return "redirect:/perfil"; 
        }

        boolean esSoloNumeros = trimmed.matches("\\d+");
        if (esSoloNumeros && trimmed.length() != 8) {
            ra.addFlashAttribute("loginError", "El DNI debe tener 8 dígitos.");
            return "redirect:/perfil"; 
        }      
        var optUsuario = usuarioService.loginClientePorIdentificador(trimmed, password);
        if (optUsuario.isEmpty()) {
            ra.addFlashAttribute("loginError", "Credenciales inválidas o usuario no permitido.");
            return "redirect:/perfil"; 
        }
        LoginUsuarioRequest request = new LoginUsuarioRequest();
        request.setNombreUsuario(optUsuario.get().getUsername()); 
        request.setPassword(password);

        try {
            JwtDto jwt = authService.login(request);
            if (jwt == null) {
                ra.addFlashAttribute("loginError", "El login ha fallado. Intente de nuevo.");
                return "redirect:/perfil"; 
            }
            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt.getJwt());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); 
            response.addCookie(jwtCookie); 
            Usuario usuario = jwtAuthenticationService.fromJwt(jwt.getJwt());
            if (usuario.getEstado() == EstadoEntidad.INACTIVO) {
                ra.addFlashAttribute("loginError", "Usuario inactivo. Contacte al administrador.");
                return "redirect:/perfil"; 
            }
            System.out.println("Usuario logueado: " + usuario.getUsername());
            ra.addFlashAttribute("loginSuccess", "Sesión iniciada correctamente.");
            return "redirect:/perfil"; //ACA ES DONDE SE VA LA DIRECCION DESPUES DE LOGIN
        } catch (Exception e) {
            ra.addFlashAttribute("loginError", "Error al iniciar sesión: " + e.getMessage());
            return "redirect:/perfil"; 
        }
    }

    // LOGOUT CLIENTE
    @GetMapping("/logout")
    public String logoutCliente(HttpSession session, RedirectAttributes ra) {
        session.removeAttribute("CLIENTE_SESION");
        ra.addFlashAttribute("info", "Has cerrado sesión.");
        return "redirect:/";
    }

    // /perfil/compras
    @GetMapping("/compras")
    public String perfilCompras(Model model) {
        model.addAttribute("pageTitle", "Mis compras");
        // TODO: lista de compras del usuario
        return "tienda/perfil/compras";
    }

    // /perfil/compras/{id}
    @GetMapping("/compras/{id}")
    public String perfilCompraDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Detalle de compra");
        model.addAttribute("idCompra", id);
        // TODO: cargar detalle de la compra por id
        return "tienda/perfil/compra-detalle";
    }

    // /perfil/favoritos
    @GetMapping("/favoritos")
    public String perfilFavoritos(Model model) {
        model.addAttribute("pageTitle", "Mis favoritos");
        // TODO: productos favoritos del usuario
        return "tienda/perfil/favoritos";
    }
}
