package com.example.simplemvc.controller.personas;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.PersonaDto;
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
}
