package com.example.simplemvc.repository;

import com.example.simplemvc.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {}
