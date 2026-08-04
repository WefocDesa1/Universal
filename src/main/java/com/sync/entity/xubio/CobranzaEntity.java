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
@Table(name = "xubio_cobranzas")
public class CobranzaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "xubio_id", nullable = false, unique = true)
    private Long xubioId;

    @Column(name = "numero")
    private String numero;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "cliente_xubio_id")
    private Long clienteXubioId;

    @Column(name = "comprobante_venta_id")
    private Long comprobanteVentaId;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "circuito_contable_id")
    private Long circuitoContableId;

    @Column(name = "utiliza_moneda_extranjera")
    private Integer utilizaMonedaExtranjera;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    @OneToMany(mappedBy = "cobranza", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CobranzaInstrumentoEntity> instrumentos = new ArrayList<>();

    @OneToMany(mappedBy = "cobranza", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CobranzaRetencionEntity> retenciones = new ArrayList<>();

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
