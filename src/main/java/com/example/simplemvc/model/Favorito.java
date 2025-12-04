package com.example.simplemvc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorito",
       uniqueConstraints = @UniqueConstraint(
         columnNames = {"persona_id", "producto_id"},
         name = "uk_favorito_persona_producto"
       ))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
