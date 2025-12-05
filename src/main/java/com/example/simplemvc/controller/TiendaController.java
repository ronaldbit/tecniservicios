package com.example.simplemvc.controller;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.FavoritoService;
import com.example.simplemvc.service.PersonaService;
import com.example.simplemvc.service.ProductoService;
import com.example.simplemvc.shared.ClienteSesion;

import com.example.simplemvc.model.Persona;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TiendaController {

    private final ProductoService productoService;
    private final PersonaService personaService;
    private final FavoritoService favoritoService;

    // HOME: "/home" → templates/tienda/index.html
    @GetMapping
    public String index(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            Model model, HttpSession session) {

        List<ProductoDto> destacados = List.of();
        List<ProductoDto> enOferta = List.of();
        List<ProductoDto> listaHome = productoService.findAll();

        model.addAttribute("pageTitle", "Tienda");
        model.addAttribute("destacados", destacados);
        model.addAttribute("enOferta", enOferta);
        model.addAttribute("listaHome", listaHome);

        model.addAttribute("query", q);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("marcaId", marcaId);
        model.addAttribute("resultados", null);

        // Por defecto, ningún favorito
        java.util.Set<Long> favoritosIds = java.util.Collections.emptySet();

        ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
        if (clienteSesion != null && clienteSesion.getIdCliente() != null) {
            favoritosIds = favoritoService.obtenerIdsFavoritosDeCliente(clienteSesion.getIdCliente());
        }

        model.addAttribute("favoritosIds", favoritosIds);
        return "tienda/index";
    }

    // DETALLE PRODUCTO: /home/producto/{id}
@GetMapping("/producto/{id}")
public String detalleProducto(@PathVariable Long id, Model model, HttpSession session) {
    ProductoDto producto = productoService.findById(id);

    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
    boolean esFavorito = false;
    java.util.Set<Long> favoritosIds = java.util.Collections.emptySet();

    if (clienteSesion != null && clienteSesion.getIdCliente() != null) {
        Long personaId = clienteSesion.getIdCliente();

        esFavorito = favoritoService.esFavorito(personaId, id);
        favoritosIds = favoritoService.obtenerIdsFavoritosDeCliente(personaId);
    }

    // productos recomendados / más productos
    var listaHome = productoService.findAll(); // o tu lógica real

    model.addAttribute("pageTitle", producto.getNombre());
    model.addAttribute("producto", producto);
    model.addAttribute("listaHome", listaHome);

    model.addAttribute("esFavorito", esFavorito);
    model.addAttribute("favoritosIds", favoritosIds);

    return "tienda/producto.detalle";
}


    // BUSCADOR: /home/buscador
    @GetMapping("/buscador")
    public String buscador(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            Model model) {

        List<ProductoDto> resultados = List.of();

        model.addAttribute("pageTitle", "Resultados de búsqueda");
        model.addAttribute("query", q);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("marcaId", marcaId);
        model.addAttribute("resultados", resultados);

        return "tienda/buscador";
    }

    // RESULTADOS POR SLUG: /home/resultado/{slug}
    @GetMapping("/resultado/{slug}")
    public String resultadosPorSlug(@PathVariable String slug, Model model) {
        String q = slug.replace("-", " ").trim();

        var resultados = productoService.findAll().stream()
                .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q.toLowerCase()))
                .toList();

        model.addAttribute("pageTitle", "Resultados para: " + q);
        model.addAttribute("query", q);
        model.addAttribute("resultados", resultados);

        return "tienda/buscador";
    }

    // BUSCADOR AJAX: /home/tienda/api/buscar
    @GetMapping("/tienda/api/buscar")
    @ResponseBody
    public List<ProductoDto> buscarPreview(@RequestParam("q") String q) {
        if (q == null || q.trim().isEmpty()) {
            return List.of();
        }
        return productoService.findAll().stream()
                .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q.toLowerCase()))
                .limit(10)
                .toList();
    }

    // CARRITO: /home/carrito
    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("pageTitle", "Carrito de compras");
        return "tienda/carrito";
    }

    // PAGAR: /home/pagar
@GetMapping("/pagar")
public String pagar(Model model, HttpSession session) {
    model.addAttribute("pageTitle", "Finalizar compra");

    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
    model.addAttribute("clienteSesion", clienteSesion);

    if (clienteSesion != null && clienteSesion.getIdCliente() != null) {
        // Usa el método que SÍ existe: obtenerEntidadPorId
        personaService.obtenerEntidadPorId(clienteSesion.getIdCliente())
                .ifPresent(persona -> model.addAttribute("persona", persona));
    } else {
        model.addAttribute("persona", null);
    }

    return "tienda/pagar";
}


}


