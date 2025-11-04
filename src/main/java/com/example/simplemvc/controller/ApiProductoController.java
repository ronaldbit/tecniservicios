package com.example.simplemvc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.ProductoDto;
import com.example.simplemvc.service.ProductoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ApiProductoController {
    private final ProductoService productoService;

//FALTA LOGICA DE SERVICIO PARA LISTAR PRODUCTOS Y LOS DEMAS ICRUDS
    @GetMapping
    public List<ProductoDto> listarProductos() {
        return productoService.findAll();
    }



    
    
        
}
