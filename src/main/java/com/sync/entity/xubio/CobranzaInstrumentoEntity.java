package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Ítem del array "transaccionInstrumentoDeCobro" de una cobranza
 * (medio con el que se cobró: tarjeta, cuenta bancaria, etc).
 */
@Data
@Entity
@Table(name = "xubio_cobranza_instrumentos")
public class CobranzaInstrumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cobranza_id", nullable = false)
    private CobranzaEntity cobranza;

    @Column(name = "transaccion_ic_id")
    private Long transaccionIcId;

    @Column(name = "cuenta_tipo")
    private Integer cuentaTipo;

    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "importe", precision = 18, scale = 2)
    private BigDecimal importe;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

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
