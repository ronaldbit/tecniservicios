package com.example.simplemvc.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.enums.EstadoCaja;

public interface CajaSesionRepository extends JpaRepository<CajaSesion, Long> {

  Optional<CajaSesion> findByCaja_IdAndEstado(Long cajaId, EstadoCaja estado);
}
