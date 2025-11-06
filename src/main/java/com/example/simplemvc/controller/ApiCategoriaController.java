package com.example.simplemvc.controller;

import com.example.simplemvc.dto.CategoriaDto;
import com.example.simplemvc.request.CrearCategoriaRequest;
import com.example.simplemvc.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class ApiCategoriaController {
    @Autowired
    private final CategoriaService categoriaService;

    @PostMapping
    public CategoriaDto crear(
            @ModelAttribute CrearCategoriaRequest request, RedirectAttributes redirectAttributes) {
        try {
            CategoriaDto categoriaDto = categoriaService.create(request);
            redirectAttributes.addFlashAttribute("categoria", categoriaDto);
            return categoriaDto;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la categoría: " + e.getMessage());
        }
    }
    @GetMapping
    public List<CategoriaDto> lista() {
        return categoriaService.findAll();
    }
}
