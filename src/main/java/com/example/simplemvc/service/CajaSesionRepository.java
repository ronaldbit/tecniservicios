package com.example.simplemvc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.CajaSesion;
import com.example.simplemvc.model.enums.EstadoCaja;

public interface CajaSesionRepository extends JpaRepository<CajaSesion, Long> {

  Optional<CajaSesion> findByCaja_IdAndEstado(Long cajaId, EstadoCaja estado);

  List<CajaSesion> findByCaja_IdAndFechaAperturaBetweenOrderByFechaAperturaDesc(Long cajaId, LocalDateTime inicio,
      LocalDateTime fin);
}
