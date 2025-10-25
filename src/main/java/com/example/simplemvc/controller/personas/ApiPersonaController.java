package com.example.simplemvc.controller.personas;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.service.PersonaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class ApiPersonaController {
  private final PersonaService personaService;

  @GetMapping
  public List<PersonaDto> lista() {
    return personaService.listaTodos();
  }
  @GetMapping("/{id}")
  public PersonaDto obtenerPorId(@PathVariable UUID id) {
    return personaService.obtenerPorId(id); 
  }
  @PostMapping
  public PersonaDto crear(@RequestBody CrearPersonaRequest request) {
    return personaService.crear(request);
  }
  @PutMapping("/{id}")
  public PersonaDto actualizar(@PathVariable UUID id, @RequestBody CrearPersonaRequest request) {
    return personaService.actualizar(id, request);
  }
  @DeleteMapping("/{id}")
  public void eliminar(@PathVariable UUID id) {
    personaService.eliminarPorId(id);
  }  
}
