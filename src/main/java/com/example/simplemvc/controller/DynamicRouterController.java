package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DynamicRouterController {

  @Autowired
  private ResourceLoader loader;

  @GetMapping("/")
  public String homeView(Model model) {
    return "tienda/home";
  }

  // NO mapear /admin/login ni /login aquí (los maneja AuthController)

  // Excluir: assets, api, captcha, favicon.ico, error, webjars, login
  @RequestMapping({
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars$).+}",
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars$).+}/{p2}",
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars$).+}/{p2}/{p3}"
  })
  public String dynamic(@PathVariable String p1,
      @PathVariable(required = false) String p2,
      @PathVariable(required = false) String p3,
      Model model) {

    String path = build(p1, p2, p3);

    if (exists("classpath:/templates/" + path + ".html")) {
      return path;
    }

    model.addAttribute("path", path);
    return "errors/404";
  }

  private String build(String... paths) {
    StringBuilder sb = new StringBuilder();

    for (String string : paths) {
      if (string != null && !string.isEmpty()) {
        if (sb.length() > 0) {
          sb.append("/");
        }
        sb.append(string);
      }
    }

    return sb.toString().replaceAll("/+$", "");
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
