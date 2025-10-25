package com.example.simplemvc.controller.producto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.model.Producto;

@RestController
@RequestMapping("/api/productos")
public class ApiProductoController {

  @GetMapping
  public List<Producto> list(@RequestParam(required = false) String q) {
    return new ArrayList<>();
  }

  @GetMapping("/{id}")
  public Producto get(@PathVariable Long id) {
    return null;
  }
}
