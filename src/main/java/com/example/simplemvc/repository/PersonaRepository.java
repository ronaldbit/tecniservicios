package com.example.simplemvc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Persona;

public interface PersonaRepository extends JpaRepository<Persona, UUID> {
}
