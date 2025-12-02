package com.example.simplemvc.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.simplemvc.model.Producto;
import com.example.simplemvc.model.enums.EstadoEntidad;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  Optional<Producto> findByNombre(String nombre);

  Optional<Producto> findByCodigo(String codigo);

  Optional<Producto> findByCategoria_IdCategoria(Long idCategoria);

  Optional<Producto> findByMarca_Id(Long idMarca);

  List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);

  @Query("SELECT p FROM Producto p WHERE " +
      "(:categoriaId IS NULL OR p.categoria.idCategoria = :categoriaId) AND " +
      "(:marcaId IS NULL OR p.marca.id = :marcaId) AND " +
      "(:estado IS NULL OR p.estado = :estado)")
  List<Producto> buscarConFiltros(@Param("categoriaId") Long categoriaId,
      @Param("marcaId") Long marcaId,
      @Param("estado") EstadoEntidad estado);

  @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo AND p.estado = 2 ORDER BY p.stockActual ASC")
  List<Producto> findProductosStockBajo();

  @Query("SELECT p FROM Producto p WHERE p.idProducto NOT IN " +
      "(SELECT d.producto.idProducto FROM VentaDetalle d JOIN d.venta v WHERE v.fechaVenta > :fechaLimite)")
  List<Producto> findProductosSinMovimiento(@Param("fechaLimite") Timestamp fechaLimite);


  
}
