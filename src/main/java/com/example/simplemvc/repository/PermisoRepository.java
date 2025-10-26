package com.example.simplemvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplemvc.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    List<Permiso> findByRolId(Long rolId);
}
