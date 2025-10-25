package com.example.simplemvc.controller.admin.api;

import com.example.simplemvc.model.ProductoModel;
import com.example.simplemvc.model.Producto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class ProductoAdminApi {
  private final ProductoModel model;
  public ProductoAdminApi(ProductoModel model){ this.model = model; }

  @PostMapping
  public Producto create(@RequestBody Producto p){
    Long id = model.crear(p);
    p.setId(id);
    return p;
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Producto p){
    int n = model.actualizar(id, p);
    return n>0? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    int n = model.eliminar(id);
    return n>0? ResponseEntity.noContent().build(): ResponseEntity.notFound().build();
  }
}
