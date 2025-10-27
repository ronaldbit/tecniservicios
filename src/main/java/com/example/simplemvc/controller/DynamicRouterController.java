package com.example.simplemvc.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.simplemvc.dto.UsuarioDto;
import com.example.simplemvc.model.Usuario;
import com.example.simplemvc.model.UsuarioMapper;
import com.example.simplemvc.service.JwtAuthenticationService;
import com.ronaldbit.Mopla;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DynamicRouterController {
  @Autowired
  private final JwtAuthenticationService jwtAuthenticationService;

  @Autowired
  private final UsuarioMapper usuarioMapper;

  @Autowired
  private final ResourceLoader loader;

  @GetMapping("/")
  @ResponseBody
  public String homeView(Model model) throws Exception {
    Mopla mopla = new Mopla("src/main/resources/templates");

    Map<String, Object> vars = new HashMap<>();
    vars.put("titulo", "Bienvenido");
    vars.put("mensaje", "Este mensaje solo aparece si showMessage es true");
    vars.put("showMessage", true);
    vars.put("users", List.of("Ronald", "User2", "Pedro"));

    return mopla.render("index.html", vars);
  }

  @RequestMapping(value = "/{path:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars$).*}/**")
  public String dynamic(
      @PathVariable String path,
      HttpServletRequest request,
      Model model) {
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
    return "/errors/404";
  }

  public boolean isAuthEntryPoint(String path) {
    for (String authPath : Arrays.asList("/auth/login", "/auth/registro", "/auth/registro-persona")) {
      if (path.equals(authPath)) {
        return true;
      }
    }
    return false;
  }

  public String authMiddleware(HttpServletRequest request, Model model) {
    String requestURI = request.getRequestURI();

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

    Usuario usuario = jwtAuthenticationService.fromJwt(jwt);
    UsuarioDto usuarioDto = usuarioMapper.toDto(usuario);

    if (usuario == null) {
      return null;
    }

    model.addAttribute("usuario", usuarioDto);

    if (!isAuthEntryPoint(requestURI)) {
      return null;
    }

    if (usuarioDto.getRoles().stream().anyMatch(role -> "ADMIN".equals(role.getNombre()))) {
      return "/dashboard";
    }

    return "/";
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