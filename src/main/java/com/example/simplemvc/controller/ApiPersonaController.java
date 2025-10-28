package com.example.simplemvc.controller;

import com.example.simplemvc.dto.PersonaDto;
import com.example.simplemvc.request.CrearPersonaRequest;
import com.example.simplemvc.service.PersonaService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/personas")
@AllArgsConstructor
public class ApiPersonaController {
  @Autowired
  private final PersonaService personaService;

  @PostMapping
  public String crear(
      @ModelAttribute CrearPersonaRequest request, RedirectAttributes redirectAttributes) {
    try {
      PersonaDto personaDto = personaService.crear(request);

      redirectAttributes.addFlashAttribute("persona", personaDto);

      return "redirect:/auth/registro";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
      return "redirect:/auth/registro-persona";
    }
  }

  @PostMapping("/crear-desde-admin")
  public ResponseEntity<?> crearDesdeAdmin(@RequestBody CrearPersonaRequest request) {
    try {
      PersonaDto personaDto = personaService.crear(request);
      return ResponseEntity.ok(personaDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }  
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public List<PersonaDto> lista() {
    return personaService.listaTodos();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public PersonaDto obtenerPorId(@PathVariable Long id) {
    return personaService.obtenerPorId(id);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CrearPersonaRequest request) {
    try {
      PersonaDto updatedPersona = personaService.actualizar(id, request);
      return ResponseEntity.ok(updatedPersona);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void eliminar(@PathVariable Long id) {
    personaService.eliminarPorId(id);
  }
}
