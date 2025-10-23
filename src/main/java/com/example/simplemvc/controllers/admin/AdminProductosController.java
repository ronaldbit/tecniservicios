package com.example.simplemvc.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductosController {
  @GetMapping public String view(){ return "admin/productos"; }
}
