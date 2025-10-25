package com.example.simplemvc.controllers.admin.api;

import com.example.simplemvc.model.UsuarioModel;
import com.example.simplemvc.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
public class UsuarioAdminApi {

    private final UsuarioModel model;
    @Autowired
    public UsuarioAdminApi(UsuarioModel model) {
        this.model = model;
    }
  
    // CRUD de USUARIOS
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return model.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return model.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> crearUsuario(@RequestBody Usuario u) {
        int filas = model.crear(u);
        if (filas > 0) {
            if (u.getIdRol() != null) {
                model.asignarRol((long) u.getIdUsuario(), u.getIdRol());
            }
            return ResponseEntity.ok("Usuario creado correctamente");
        }
        return ResponseEntity.badRequest().body("No se pudo crear el usuario");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario u) {
        int filas = model.actualizar(id, u);
        return filas > 0
                ? ResponseEntity.ok("Usuario actualizado correctamente")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        int filas = model.eliminar(id);
        return filas > 0
                ? ResponseEntity.ok("Usuario eliminado correctamente")
                : ResponseEntity.notFound().build();
    }

    //Gestion Roles
    @PostMapping("/roles")
    public ResponseEntity<String> crearRol(
            @RequestParam String nombreRol,
            @RequestParam String descripcion
    ) {
        int filas = model.crearRol(nombreRol, descripcion);
        return filas > 0
                ? ResponseEntity.ok("Rol creado correctamente")
                : ResponseEntity.badRequest().body("No se pudo crear el rol");
    }

    @GetMapping("/roles")
    public List<Map<String, Object>> listarRoles() {
        return model.listarRoles();
    }

    @PostMapping("/{idUsuario}/roles/{idRol}")
    public ResponseEntity<String> asignarRol(
            @PathVariable Long idUsuario,
            @PathVariable Long idRol
    ) {
        int filas = model.asignarRol(idUsuario, idRol);
        return filas > 0
                ? ResponseEntity.ok("Rol asignado correctamente")
                : ResponseEntity.badRequest().body("No se pudo asignar el rol");
    }
}
