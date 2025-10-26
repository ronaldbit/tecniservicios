package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.UsuarioRol;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, Long> {
    UsuarioRol findByNombre(String nombre);    
}
