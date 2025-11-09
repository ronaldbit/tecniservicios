package com.example.simplemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.simplemvc.model.ProductoOnline;
import java.util.Optional;

public interface ProductoOnlineRepository extends JpaRepository<ProductoOnline, Long> {
    Optional<ProductoOnline> findByProducto_IdProducto(Long idProducto);
}
