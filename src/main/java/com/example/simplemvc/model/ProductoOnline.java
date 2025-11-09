package com.example.simplemvc.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "productos_online")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoOnline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_online")
    private Long idProductoOnline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "precio_online", precision = 12, scale = 2, nullable = false)
    private BigDecimal precioOnline;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagenes", columnDefinition = "LONGTEXT")
    private String imagenes;

    @Column(name = "destacado", nullable = false)
    private boolean destacado;

    @Column(name = "publicado", nullable = false)
    private boolean publicado;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true)
    private Timestamp updatedAt;
}
