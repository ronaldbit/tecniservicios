package com.example.simplemvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class TiendaController {

  private final ProductoService productoService;

  // HOME: "/" → templates/tienda/index.html
  @GetMapping
  public String index(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Long categoriaId,
      @RequestParam(required = false) Long marcaId,
      Model model) {
    // TODO: reemplazar por consultas reales
    List<ProductoDto> destacados = List.of();
    List<ProductoDto> enOferta = List.of();
    List<ProductoDto> listaHome = productoService.findAll(); // o List.of() si prefieres

    model.addAttribute("pageTitle", "Tienda");
    model.addAttribute("destacados", destacados);
    model.addAttribute("enOferta", enOferta);
    model.addAttribute("listaHome", listaHome);

    model.addAttribute("query", q);
    model.addAttribute("categoriaId", categoriaId);
    model.addAttribute("marcaId", marcaId);
    model.addAttribute("resultados", null); // luego para /buscador

    return "tienda/index";
  }

  // DETALLE PRODUCTO: /producto/{id}
  @GetMapping("/producto/{id}")
  public String detalleProducto(@PathVariable Long id,
      Model model,
      RedirectAttributes ra) {
    try {
      ProductoDto producto = productoService.findById(id);
      model.addAttribute("producto", producto);
      model.addAttribute("pageTitle", producto.getNombre());
      // IMPORTANTE: este nombre debe coincidir con el archivo .html
      return "tienda/producto.detalle";
    } catch (IllegalArgumentException e) {
      // Producto no existe → volvemos al home sin reventar
      ra.addFlashAttribute("error", e.getMessage());
      return "redirect:/home";
    } catch (Exception e) {
      // Cualquier otra cosa rara
      ra.addFlashAttribute("error", "Ocurrió un error al cargar el producto.");
      return "redirect:/home";
    }
  }

  // BUSCADOR: /buscador (puede ser igual que "/" pero solo resultados) - Duda si
  // segira funcionando
  @GetMapping("/buscador")
  public String buscador(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Long categoriaId,
      @RequestParam(required = false) Long marcaId,
      Model model) {
    // TODO: implementar búsqueda real
    List<ProductoDto> resultados = List.of();

    model.addAttribute("pageTitle", "Resultados de búsqueda");
    model.addAttribute("query", q);
    model.addAttribute("categoriaId", categoriaId);
    model.addAttribute("marcaId", marcaId);
    model.addAttribute("resultados", resultados);

    return "tienda/buscador";
  }

  @GetMapping("/resultado/{slug}")
  public String resultadosPorSlug(@PathVariable String slug, Model model) {
    // convertir slug → texto búsqueda
    String q = slug.replace("-", " ").trim();

    var resultados = productoService.findAll().stream()
        .filter(p -> p.getNombre() != null &&
            p.getNombre().toLowerCase().contains(q.toLowerCase()))
        .toList();

    model.addAttribute("pageTitle", "Resultados para: " + q);
    model.addAttribute("query", q);
    model.addAttribute("resultados", resultados);

    return "tienda/buscador";
  }

  // buscador ajax
  @GetMapping("/tienda/api/buscar")
  @ResponseBody
  public java.util.List<com.example.simplemvc.dto.ProductoDto> buscarPreview(@RequestParam("q") String q) {
    if (q == null || q.trim().isEmpty()) {
      return java.util.List.of();
    }
    // Versión simple: busca en todos y filtra por nombre
    return productoService.findAll().stream()
        .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q.toLowerCase())).limit(10).toList();
  }

  // CARRITO: /carrito
  @GetMapping("/carrito")
  public String carrito(Model model) {
    model.addAttribute("pageTitle", "Carrito de compras");
    // Luego aquí metes items desde JS / sesión / API
    return "tienda/carrito";
  }

  // PAGAR: /pagar
  @GetMapping("/pagar")
  public String pagar(Model model) {
    model.addAttribute("pageTitle", "Pagar");
    // Luego aquí muestras resumen, formulario de datos, etc.
    return "tienda/pagar";
  }
}
