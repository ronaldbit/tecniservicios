package com.example.simplemvc.repository;

import com.example.simplemvc.model.Persona;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByNumeroDocumento(String numeroDocumento);
}
