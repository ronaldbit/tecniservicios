package com.example.simplemvc.controller.admin.api;

import com.example.simplemvc.model.Persona;
import com.example.simplemvc.model.PersonaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/personas")
public class PersonaAdminApi {

    @Autowired
    private PersonaModel personaModel;

    @GetMapping
    public List<Persona> listarPersonas() {
        return personaModel.listar();
    }
    @GetMapping("/{id}")
    public Persona obtenerPersonaPorId(@PathVariable Long id) {
        return personaModel.buscarPorId(id);
    }
    @PostMapping
    public String crearPersona(@RequestBody Persona persona) {
        Long nuevoId = personaModel.crear(persona);
        return "Persona creada con ID: " + nuevoId;
    }
    @PutMapping("/{id}")
    public String actualizarPersona(@PathVariable Long id, @RequestBody Persona persona) {
        int filas = personaModel.actualizar(id, persona);
        return filas > 0 ? "Persona actualizada correctamente." : "No se encontró la persona.";
    }
    @DeleteMapping("/{id}")
    public String eliminarPersona(@PathVariable Long id) {
        int filas = personaModel.eliminar(id);
        return filas > 0 ? "Persona eliminada correctamente." : "No se encontró la persona.";
    }
}
