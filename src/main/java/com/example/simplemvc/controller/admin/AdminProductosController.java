package com.example.simplemvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/Logistica/Productos")
public class AdminProductosController {
  @GetMapping public String view(){ return "admin/Logistica/Productos"; }
}
