package com.example.simplemvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class PaginasPublicasController {

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros(Model model) {
        model.addAttribute("pageTitle", "Sobre nosotros");
        return "tienda/pages/sobre-nosotros";
    }

    @GetMapping("/politicas")
    public String politicas(Model model) {
        model.addAttribute("pageTitle", "Políticas");
        return "tienda/pages/politicas";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("pageTitle", "Contacto");
        return "tienda/pages/contacto";
    }

    @GetMapping("/marcas")
    public String marcas(Model model) {
        model.addAttribute("pageTitle", "Marcas");
        return "tienda/pages/marcas";
    }

    @GetMapping("/categorias")
    public String categorias(Model model) {
        model.addAttribute("pageTitle", "Categorías");
        return "tienda/pages/categorias";
    }
}
