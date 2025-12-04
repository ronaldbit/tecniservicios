package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.simplemvc.dto.JwtDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.request.CrearUsuarioClienteRequest;
import com.example.simplemvc.request.LoginUsuarioRequest;
import com.example.simplemvc.service.AuthService;
import com.example.simplemvc.service.FavoritoService;
import com.example.simplemvc.service.UsuarioService;
import com.example.simplemvc.service.VentaService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.dto.VentaDto;
import com.example.simplemvc.service.PersonaService;
import com.example.simplemvc.shared.ClienteSesion;


@Controller
@RequestMapping("/home/perfil")
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE) // dejalo pa que dynamic no interfira dskfjha
public class PerfilController {

  private final UsuarioService usuarioService;
    private final PersonaService personaService;
  private final VentaService ventaService;
private final FavoritoService favoritoService;

  @Autowired
  private final AuthService authService;


  // /perfil -> aquí luego pondrás perfil o formulario de registro según sesión
@GetMapping
public String perfilInicio(Model model, HttpSession session) {
    model.addAttribute("pageTitle", "Mi perfil"); model.addAttribute("seccion", "resumen");

    if (!model.containsAttribute("usuarioRequest")) {
      model.addAttribute("usuarioRequest", new CrearUsuarioClienteRequest());
    }
    if (!model.containsAttribute("login")) {
      model.addAttribute("login", new LoginUsuarioRequest());
    }

    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");
    if (clienteSesion != null && clienteSesion.getIdCliente() != null) {
        var recientes = ventaService
                .listarVentasDeClientePorDni(clienteSesion.getDni())
                .stream()
                .limit(5)
                .toList();
        model.addAttribute("comprasRecientes", recientes);
    }

    return "tienda/perfil/index";
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

    try {
      var optUsuario = usuarioService.loginClientePorIdentificador(trimmed, password);

      if (optUsuario.isEmpty()) {
        ra.addFlashAttribute("loginError", "Credenciales inválidas o usuario no permitido.");
        return redirect(origen);
      }

      Usuario usuario = optUsuario.get();

      if ("perfil".equals(origen)) {

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

      ra.addFlashAttribute("loginSuccess", "Sesión iniciada correctamente.");
      return redirect(origen);
    } catch (Exception e) {
      ra.addFlashAttribute("loginError", e.getMessage());
      return redirect(origen);
    }
  }

  private String redirect(String origen) {
    if ("menu".equals(origen)) {
      return "redirect:/home";
    }
    return "redirect:/home/perfil";
  }

  // LOGOUT CLIENTE
  @GetMapping("/logout")
  public String logoutCliente(HttpSession session, RedirectAttributes ra) {
    session.removeAttribute("ClienteSesion");
    ra.addFlashAttribute("info", "Has cerrado sesión.");
    return "redirect:/auth/logout";
  }

  // /perfil/compras
@GetMapping("/compras")
public String perfilCompras(Model model, HttpSession session, RedirectAttributes ra) {
    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");

    if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
        ra.addFlashAttribute("info", "Primero inicia sesión para ver tus compras.");
        return "redirect:/home/perfil";
    }

    // usamos el DNI que guarda ClienteSesion
    String dni = clienteSesion.getDni();
    var compras = ventaService.listarVentasDeClientePorDni(dni);

    model.addAttribute("pageTitle", "Mis compras"); model.addAttribute("seccion", "compras");
    model.addAttribute("compras", compras);

    return "tienda/perfil/compras";
}


  // /perfil/compras/{id}
@GetMapping("/compras/{id}")
public String perfilCompraDetalle(
        @PathVariable Long id,
        Model model,
        HttpSession session,
        RedirectAttributes ra) {

    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");

    if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
        ra.addFlashAttribute("info", "Primero inicia sesión para ver el detalle de la compra.");
        return "redirect:/home/perfil";
    }

    VentaDto venta = ventaService.obtenerVentaPorId(id);

    // seguridad básica: que la venta pertenezca al cliente logueado
    if (venta == null ||
        venta.getClienteNumeroDocumento() == null ||
        !venta.getClienteNumeroDocumento().equals(clienteSesion.getDni())) {

        ra.addFlashAttribute("error", "No se encontró la compra o no pertenece a tu cuenta.");
        return "redirect:/home/perfil/compras";
    }

    model.addAttribute("pageTitle", "Detalle de compra"); model.addAttribute("seccion", "compras");
    model.addAttribute("venta", venta);

    return "tienda/perfil/compra-detalle";
}


// /perfil/favoritos
@GetMapping("/favoritos")
public String perfilFavoritos(Model model, HttpSession session, RedirectAttributes ra) {
    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");

    if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
        ra.addFlashAttribute("info", "Inicia sesión para ver tus favoritos.");
        return "redirect:/home/perfil";
    }

    var favoritos = favoritoService.listarFavoritosCliente(clienteSesion.getIdCliente());

    model.addAttribute("pageTitle", "Mis favoritos");
    model.addAttribute("productosFavoritos", favoritos);
    model.addAttribute("favoritosIds",
            favoritos.stream().map(ProductoDto::getIdProducto).collect(java.util.stream.Collectors.toSet()));

    model.addAttribute("seccion", "favoritos");
    return "tienda/perfil/favoritos";
}


@GetMapping("/editar")
public String editarPerfil(Model model, HttpSession session, RedirectAttributes ra) {
    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");

    if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
        ra.addFlashAttribute("info", "Primero inicia sesión para editar tu perfil.");
        return "redirect:/home/perfil";
    }

    PersonaDto personaDto = personaService.obtenerPorId(clienteSesion.getIdCliente());

    // Si aún no hay objeto en el modelo, lo ponemos
    if (!model.containsAttribute("persona")) {
        model.addAttribute("persona", personaDto);
    }

    model.addAttribute("pageTitle", "Editar perfil");model.addAttribute("seccion", "editar");
    return "tienda/perfil/editar";
}
@PostMapping("/editar")
public String guardarPerfil(
        PersonaDto personaForm,   // viene del th:object
        HttpSession session,
        RedirectAttributes ra) {

    ClienteSesion clienteSesion = (ClienteSesion) session.getAttribute("clienteSesion");

    if (clienteSesion == null || clienteSesion.getIdCliente() == null) {
        ra.addFlashAttribute("info", "Tu sesión expiró. Inicia sesión nuevamente.");
        return "redirect:/home/perfil";
    }

    try {
        // Aseguramos que el ID sea el de la sesión
        personaForm.setId(clienteSesion.getIdCliente());
        personaService.actualizarDesdePerfil(personaForm);

        // Actualizamos nombre/email en la sesión para que el dashboard los muestre actualizados
        String nombreCompleto = ((personaForm.getNombres() != null ? personaForm.getNombres() : "") + " " +
                                 (personaForm.getApellidos() != null ? personaForm.getApellidos() : "")).trim();
        clienteSesion.setNombreCompleto(nombreCompleto);
        clienteSesion.setEmail(personaForm.getEmail());
        session.setAttribute("clienteSesion", clienteSesion);

        ra.addFlashAttribute("success", "Perfil actualizado correctamente.");
        return "redirect:/home/perfil";
    } catch (Exception e) {
        ra.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        return "redirect:/home/perfil/editar";
    }
}

}
