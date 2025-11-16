package com.example.simplemvc.controller;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.dto.RolDto;
import com.example.simplemvc.request.CrearPermisoRequest;
import com.example.simplemvc.request.CrearUsuarioRol;
import com.example.simplemvc.service.PermisoService;
import com.example.simplemvc.service.RolService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles-permisos")
@RequiredArgsConstructor
public class ApiRolesPermisos {

  private final RolService rolService;
  private final PermisoService permisoService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<RolDto>> listarRoles() {
    return ResponseEntity.ok(rolService.lista());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RolDto> obtenerRolPorId(@PathVariable Long id) {
    return ResponseEntity.ok(rolService.obtenerPorId(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> crearRol(@RequestBody CrearUsuarioRol request) {
    try {
      RolDto nuevoRol = rolService.crear(request);
      return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    } catch (Exception e) {
      String errorMessage = "EL NOMBRE DE ROL YA ESTA EN USO";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> actualizarRol(
      @PathVariable Long id, @RequestBody CrearUsuarioRol request) {
    try {
      RolDto rolActualizado = rolService.actualizar(id, request);
      return ResponseEntity.ok(rolActualizado);
    } catch (Exception e) {
      String errorMessage = "EL NOMBRE DE ROL YA ESTA EN USO";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<?> eliminarRol(@PathVariable Long id) {
    try {
      rolService.eliminarPorId(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      String errorMessage = "NO SE PUEDE ELIMINAR EL ROL PORQUE ESTA SIENDO USADO POR USUARIOS";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
  }

  // Gestion de permisos solamente
  @GetMapping("/{rolId}/permisos")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<PermisoDto>> obtenerPermisosPorRolId(@PathVariable Long rolId) {
    return ResponseEntity.ok(permisoService.obtenerPermisosPorRolId(rolId));
  }

  @PostMapping("/{rolId}/permisos")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> crearPermisoParaRol(
      @PathVariable Long rolId, @RequestBody CrearPermisoRequest request) {
    request.setRolId(rolId);
    permisoService.crearPermiso(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/permisos/{permisoId}")
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void actualizarPermiso(
      @PathVariable Long permisoId, @RequestBody CrearPermisoRequest request) {
    permisoService.actualizarPermiso(permisoId, request);
  }

  @DeleteMapping("/permisos/{permisoId}")
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarPermiso(@PathVariable Long permisoId) {
    permisoService.eliminarPermiso(permisoId);
  }
}
