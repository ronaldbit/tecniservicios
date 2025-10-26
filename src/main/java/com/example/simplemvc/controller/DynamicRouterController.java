package com.example.simplemvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DynamicRouterController {

  @Autowired
  private ResourceLoader loader;

  @GetMapping("/")
  public String homeView(Model model) {
    return "tienda/home";
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

  private boolean exists(String loc) {
    try {
      Resource r = loader.getResource(loc);
      return r != null && r.exists();
    } catch (Exception e) {
      return false;
    }
  }
}
