package com.example.simplemvc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.service.PersonaService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/api/personas")
@AllArgsConstructor
public class ApiPersonaController {
  @Autowired
  private final PersonaService personaService;

  @PostMapping
  public String crear(@ModelAttribute CrearPersonaRequest request, HttpServletResponse response,
      Model model) {
    try {
      System.out.println(request);
      PersonaDto personaDto = personaService.crear(request);

      model.addAttribute("persona", personaDto);

      System.out.println("Persona creada con ID: " + personaDto.getId());
      return "/auth/registro";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "/auth/registro-persona";
    }
  }

  @GetMapping
  public List<PersonaDto> lista() {
    return personaService.listaTodos();
  }

  @GetMapping("/{id}")
  public PersonaDto obtenerPorId(@PathVariable Long id) {
    return personaService.obtenerPorId(id);
  }

  @PutMapping("/{id}")
  public PersonaDto actualizar(@PathVariable Long id, @RequestBody CrearPersonaRequest request) {
    return personaService.actualizar(id, request);
  }

  @DeleteMapping("/{id}")
  public void eliminar(@PathVariable Long id) {
    personaService.eliminarPorId(id);
  }
}
