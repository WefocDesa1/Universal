package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "xubio_pagos")
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** transaccionid del JSON */
    @Column(name = "xubio_id", nullable = false, unique = true)
    private Long xubioId;

    @Column(name = "circuito_contable_id")
    private Long circuitoContableId;

    @Column(name = "proveedor_xubio_id")
    private Long proveedorXubioId;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "numero_recibo")
    private String numeroRecibo;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "utiliza_moneda_extranjera")
    private Integer utilizaMonedaExtranjera;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoInstrumentoEntity> instrumentos = new ArrayList<>();

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoRetencionEntity> retenciones = new ArrayList<>();

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
