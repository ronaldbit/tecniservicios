package com.example.simplemvc.controller;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.request.CrearUsuarioClienteRequest;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.shared.ClienteSesion;
import com.example.simplemvc.service.AuthService;
import com.example.simplemvc.service.UsuarioService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/login")
    public String loginCliente(
            @RequestParam("identificador") String identificador,
            @RequestParam("password") String password,
            @RequestParam(value = "origen", required = false, defaultValue = "perfil") String origen,
            RedirectAttributes ra,
            HttpServletResponse response,
            HttpSession session) {

        String trimmed = identificador == null ? "" : identificador.trim();

        if (trimmed.isEmpty() || password == null || password.isBlank()) {
            ra.addFlashAttribute("loginError", "Ingresa tu DNI o correo y la contraseña.");
            return redirect(origen);
        }
        boolean esSoloNumeros = trimmed.matches("\\d+");
        if (esSoloNumeros && trimmed.length() != 8) {
            ra.addFlashAttribute("loginError", "El DNI debe tener 8 dígitos.");
            return redirect(origen);
        }

        if (!esSoloNumeros && !trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            ra.addFlashAttribute("loginError", "Ingresa un correo válido.");
            return redirect(origen);
        }

        try {
            var optUsuario = usuarioService.loginClientePorIdentificador(trimmed, password);

            if (optUsuario.isEmpty()) {
                ra.addFlashAttribute("loginError", "Credenciales inválidas o usuario no permitido.");
                return redirect(origen);
            }

            Usuario usuario = optUsuario.get();

            if (origen.equals("perfil")) {

                LoginUsuarioRequest request = new LoginUsuarioRequest();
                request.setNombreUsuario(usuario.getUsername());
                request.setPassword(password);

                try {
                    JwtDto jwt = authService.login(request);

                    if (jwt == null) {
                        ra.addFlashAttribute("loginError", "El login ha fallado. Intente de nuevo.");
                        return redirect(origen);
                    }

                    Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt.getJwt());
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(24 * 60 * 60);
                    response.addCookie(jwtCookie);

                } catch (Exception e) {
                    ra.addFlashAttribute("loginError", "Error al iniciar sesión: " + e.getMessage());
                    return redirect(origen);
                }
            }
            var persona = usuario.getPersona();
            ClienteSesion clienteSesion = new ClienteSesion();
            clienteSesion.setIdCliente(persona.getId());
            clienteSesion.setNombreCompleto(
                    (persona.getNombres() != null ? persona.getNombres() : "") + " " +
                            (persona.getApellidos() != null ? persona.getApellidos() : ""));
            clienteSesion.setEmail(persona.getEmail());
            clienteSesion.setDni(persona.getNumeroDocumento());
            session.setAttribute("clienteSesion", clienteSesion);

            ra.addFlashAttribute("loginSuccess", "Sesión iniciada correctamente.");
            return redirect(origen);
        } catch (Exception e) {         
            ra.addFlashAttribute("loginError", e.getMessage());
            return redirect(origen);
        }
    }

    private String redirect(String origen) {
        if (origen.equals("menu")) {
            return "redirect:/";
        }
        return "redirect:/perfil";
    }

    // LOGOUT CLIENTE
    @GetMapping("/logout")
    public String logoutCliente(HttpSession session, RedirectAttributes ra) {
        session.removeAttribute("CLIENTE_SESION");
        ra.addFlashAttribute("info", "Has cerrado sesión.");
        return "redirect:/auth/logout";
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
