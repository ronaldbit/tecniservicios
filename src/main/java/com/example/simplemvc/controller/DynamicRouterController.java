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
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars|login$).+}",
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars|login$).+}/{p2}",
      "/{p1:^(?!assets|assets_shop|api|captcha|favicon\\.ico$|error|errors$|webjars|login$).+}/{p2}/{p3}"
  })
  public String dynamic(@PathVariable String p1,
      @PathVariable(required = false) String p2,
      @PathVariable(required = false) String p3,
      Model model) {

    String path = build(p1, p2, p3);
    boolean admin = path.startsWith("admin/");
    String tpl = admin ? path : "tienda/" + path;

    if (exists("classpath:/templates/" + tpl + ".html"))
      return tpl;
    if (admin && exists("classpath:/templates/admin/index.html"))
      return "admin/index";

    model.addAttribute("path", path);
    return "errors/404";
  }

  private String build(String p1, String p2, String p3) {
    StringBuilder sb = new StringBuilder();
    if (p1 != null)
      sb.append(p1);
    if (p2 != null)
      sb.append("/").append(p2);
    if (p3 != null)
      sb.append("/").append(p3);
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
