package com.example.simplemvc.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.simplemvc.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombre(String nombre);
    Optional<Producto> findByCodigo(String codigo);
    Optional<Producto> findByCategoria_IdCategoria(Long idCategoria);
    Optional<Producto> findByMarca_Id(Long idMarca);
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);

}
