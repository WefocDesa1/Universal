package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Catálogo de depósitos. Xubio no expone un endpoint propio para listarlos:
 * se completa a medida que aparecen referenciados en otras entidades.
 */
@Data
@Entity
@Table(name = "xubio_depositos")
public class DepositoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "xubio_id", nullable = false, unique = true)
    private Long xubioId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
