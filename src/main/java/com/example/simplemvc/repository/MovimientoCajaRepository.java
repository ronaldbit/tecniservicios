package com.example.simplemvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.MovimientoCaja;

public interface MovimientoCajaRepository extends JpaRepository<MovimientoCaja, Long> {
  List<MovimientoCaja> findBySesion_IdOrderByFechaDesc(Long sesionId);

}
