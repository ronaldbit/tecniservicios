package com.example.simplemvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    // /perfil
    @GetMapping
    public String perfilInicio(Model model) {
        model.addAttribute("pageTitle", "Mi perfil");
        // Aquí luego metes datos del usuario logueado
        return "tienda/perfil/index";
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
