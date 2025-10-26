package com.example.simplemvc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simplemvc.dto.PermisoDto;
import com.example.simplemvc.dto.UsuarioRolDto;
import com.example.simplemvc.request.CrearPermisoRequest;
import com.example.simplemvc.request.CrearUsuarioRol;
import com.example.simplemvc.service.PermisoService;
import com.example.simplemvc.service.UsuarioRolService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles-permisos") 
@RequiredArgsConstructor
public class ApiRoles_Permisos {
  
    private final UsuarioRolService usuarioRolService;
    private final PermisoService permisoService;

    // Gestión de roles de usuario
    @GetMapping
    public ResponseEntity<List<UsuarioRolDto>> listarRoles() {
        return ResponseEntity.ok(usuarioRolService.listarRoles());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRolDto> obtenerRolPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioRolService.obtenerPorId(id));
    }


    @PostMapping
    public ResponseEntity<UsuarioRolDto> crearRol(@RequestBody CrearUsuarioRol request) {
        UsuarioRolDto nuevoRol = usuarioRolService.crear(request);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRolDto> actualizarRol(@PathVariable Long id, @RequestBody CrearUsuarioRol request) {
        UsuarioRolDto rolActualizado = usuarioRolService.actualizar(id, request);
        return ResponseEntity.ok(rolActualizado);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarRol(@PathVariable Long id) {
        usuarioRolService.eliminarPorId(id);
    }
  

    //Gestion de permisos solamente
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