package com.example.simplemvc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.dto.RolDto;
import com.example.simplemvc.request.CrearPermisoRequest;
import com.example.simplemvc.request.CrearUsuarioRol;
import com.example.simplemvc.service.PermisoService;
import com.example.simplemvc.service.RolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles-permisos")
@RequiredArgsConstructor
public class ApiRolesPermisos {

  private final RolService rolService;
  private final PermisoService permisoService;

  // Gestión de roles de usuario
  @GetMapping
  public ResponseEntity<List<RolDto>> listarRoles() {
    return ResponseEntity.ok(rolService.lista());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RolDto> obtenerRolPorId(@PathVariable Long id) {
    return ResponseEntity.ok(rolService.obtenerPorId(id));
  }

  @PostMapping
  public ResponseEntity<RolDto> crearRol(@RequestBody CrearUsuarioRol request) {
    RolDto nuevoRol = rolService.crear(request);
    return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RolDto> actualizarRol(@PathVariable Long id, @RequestBody CrearUsuarioRol request) {
    RolDto rolActualizado = rolService.actualizar(id, request);
    return ResponseEntity.ok(rolActualizado);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarRol(@PathVariable Long id) {
    rolService.eliminarPorId(id);
  }

  // Gestion de permisos solamente
  @GetMapping("/{rolId}/permisos")
  public ResponseEntity<List<PermisoDto>> obtenerPermisosPorRolId(@PathVariable Long rolId) {
    return ResponseEntity.ok(permisoService.obtenerPermisosPorRolId(rolId));
  }

  @PostMapping("/{rolId}/permisos")
  public ResponseEntity<Void> crearPermisoParaRol(@PathVariable Long rolId,
      @RequestBody CrearPermisoRequest request) {
    request.setRolId(rolId);
    permisoService.crearPermiso(request);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/permisos/{permisoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void actualizarPermiso(
      @PathVariable Long permisoId,
      @RequestBody CrearPermisoRequest request) {

    permisoService.actualizarPermiso(permisoId, request);
  }

  @DeleteMapping("/permisos/{permisoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarPermiso(@PathVariable Long permisoId) {
    permisoService.eliminarPermiso(permisoId);
  }
}