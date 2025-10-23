package com.example.simplemvc.controllers.tienda.api;

import com.example.simplemvc.model.ProductoModel;
import com.example.simplemvc.model.Producto;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/tienda")
public class ProductoPublicApi {
  private final ProductoModel model;
  public ProductoPublicApi(ProductoModel model){ this.model = model; }

  @GetMapping("/products")
  public List<Producto> list(@RequestParam(required=false) String q){ return model.listarPublico(q); }

  @GetMapping("/products/{id}")
  public Producto get(@PathVariable Long id){ return model.obtener(id).orElseThrow(); }
}
