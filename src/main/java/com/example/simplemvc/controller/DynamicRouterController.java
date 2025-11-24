package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.service.GetActualUsuarioService;
import com.example.simplemvc.service.JwtAuthenticationService;

//import com.ronaldbit.Mopla;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DynamicRouterController {
  @Autowired
  private final JwtAuthenticationService jwtAuthenticationService;

  @Autowired
  private final ResourceLoader loader;

  @Autowired
  private final UsuarioMapper usuarioMapper;

  @Autowired
  private final GetActualUsuarioService getActualUsuarioService;

  // @GetMapping("/")
  // @ResponseBody
  // public String homeView(Model model) throws Exception { }

  @GetMapping("/")
  public String root() {
    return "redirect:/home";
  }

  @RequestMapping(value = "/{path:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|webjars|uploads$).*}/**")
  public String dynamic(@PathVariable String path, HttpServletRequest request, Model model) {
    String requestURI = request.getRequestURI();

    if (requestURI.endsWith("/") && requestURI.length() > 1) {
      String redirectPath = requestURI.substring(0, requestURI.length() - 1);
      return "redirect:" + redirectPath;
    }

    String redirectAuth = authMiddleware(request, model);
    if (redirectAuth != null) {
      return "redirect:" + redirectAuth;
    }

    String relativePath = requestURI.startsWith("/") ? requestURI.substring(1) : requestURI;

    if (exists("classpath:/templates/" + relativePath + ".html")) {
      return relativePath;
    }

    if (exists("classpath:/templates/" + relativePath + "/index.html")) {
      return relativePath + "/index";
    }

    model.addAttribute("path", relativePath);
    return "errors/404";
  }

  public String authMiddleware(HttpServletRequest request, Model model) {
    Cookie[] cookies = request.getCookies();
    String jwt = null;

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("JWT_TOKEN".equals(cookie.getName())) {
          jwt = cookie.getValue();
          break;
        }
      }
    }

    if (jwt == null || jwt.isEmpty()) {
      return null;
    }

    Usuario usuario = jwtAuthenticationService.fromJwt(jwt);
    if (usuario == null) {
      return null;
    }

    Usuario actualUsuario = getActualUsuarioService.get();
    if (actualUsuario == null) {
      return null;
    }

    model.addAttribute("usuario", usuarioMapper.toDto(actualUsuario));
    actualUsuario.getRoles().stream()
        .flatMap(rol -> rol.getPermisos().stream())
        .forEach(permiso -> model.addAttribute("hide" + permiso.getNombre(), false));

    return null;

  }

  private boolean exists(String loc) {
    try {
      Resource r = loader.getResource(loc);
      return r != null && r.exists();
    } catch (Exception e) {
      return false;
    }
  }
}
