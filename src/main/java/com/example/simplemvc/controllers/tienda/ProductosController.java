package com.example.simplemvc.controllers.tienda;

import com.example.simplemvc.model.ProductoModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductosController {

  private final ProductoModel model;
  public ProductosController(ProductoModel model){ this.model = model; }

  @GetMapping
  public String view(Model modelTh){ return "tienda/productos"; }
}
