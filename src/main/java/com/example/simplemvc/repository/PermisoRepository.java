package com.example.simplemvc.repository;

import com.example.simplemvc.model.Permiso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
  List<Permiso> findByRolId(Long rolId);
}
