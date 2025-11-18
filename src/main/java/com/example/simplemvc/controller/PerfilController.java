package com.example.simplemvc.controller;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.shared.ClienteSesion;
import com.example.simplemvc.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;

    // /perfil  -> aquí luego pondrás perfil o formulario de registro según sesión
    @GetMapping
    public String perfilInicio(Model model, HttpSession session) {
        model.addAttribute("pageTitle", "Mi perfil");
        // clienteSesion ya viene de GlobalModelAttributes, no hace falta setear aquí
        return "tienda/perfil/index";
    }

    // LOGIN CLIENTE (desde menú/header)
    @PostMapping("/login")
    public String loginCliente(
            @RequestParam("identificador") String identificador,
            @RequestParam("password") String password,
            RedirectAttributes ra,
            HttpSession session
    ) {
        String trimmed = identificador == null ? "" : identificador.trim();

        if (trimmed.isEmpty() || password == null || password.isBlank()) {
            ra.addFlashAttribute("loginError", "Ingresa tu DNI o correo y la contraseña.");
            return "redirect:/";
        }

        boolean esSoloNumeros = trimmed.matches("\\d+");
        if (esSoloNumeros && trimmed.length() != 8) {
            ra.addFlashAttribute("loginError", "El DNI debe tener 8 dígitos.");
            return "redirect:/";
        }
        if (!esSoloNumeros && !trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            ra.addFlashAttribute("loginError", "Ingresa un correo válido.");
            return "redirect:/";
        }

        var optUsuario = usuarioService.loginClientePorIdentificador(trimmed, password);
        if (optUsuario.isEmpty()) {
            ra.addFlashAttribute("loginError", "Credenciales inválidas o usuario no permitido.");
            return "redirect:/";
        }

        Usuario usuario = optUsuario.get();
        var persona = usuario.getPersona();

        ClienteSesion clienteSesion = new ClienteSesion();
        clienteSesion.setIdCliente(persona.getId()); // o usuario.getId(), como prefieras
        clienteSesion.setNombreCompleto(
                (persona.getNombres() != null ? persona.getNombres() : "") + " " +
                (persona.getApellidos() != null ? persona.getApellidos() : "")
        );
        clienteSesion.setEmail(persona.getEmail());
        clienteSesion.setDni(persona.getNumeroDocumento());

        session.setAttribute("CLIENTE_SESION", clienteSesion);
        ra.addFlashAttribute("loginSuccess", "Sesión iniciada correctamente.");

        return "redirect:/perfil";
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
