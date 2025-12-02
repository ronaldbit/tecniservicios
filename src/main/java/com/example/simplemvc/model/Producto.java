package com.example.simplemvc.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.simplemvc.model.enums.EstadoEntidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_producto")
  private Long idProducto;

  @Column(name = "codigo", nullable = false, length = 60, unique = true)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 200)
  private String nombre;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_marca")
  private Marca marca;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_categoria")
  private Categoria categoria;

  @Column(name = "precio", nullable = false, precision = 12, scale = 2)
  private BigDecimal precio;

  @Column(name = "unidad", length = 20)
  private String unidad;

  @Column(name = "stock_minimo", precision = 12, scale = 2)
  private BigDecimal stockMinimo;

  @Column(name = "stock_actual", precision = 12, scale = 2)
  @Builder.Default
  private BigDecimal stockActual = BigDecimal.ZERO;

  @Column(name = "precio_online", precision = 12, scale = 2, nullable = false)
  private BigDecimal precioOnline;

  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "imagenes", columnDefinition = "longtext")
  private String imagenes;

  @Column(name = "destacado", nullable = false)
  private boolean destacado;

  @Builder.Default
  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private EstadoEntidad estado = EstadoEntidad.ACTIVO;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = true)
  private Timestamp updatedAt;
}
