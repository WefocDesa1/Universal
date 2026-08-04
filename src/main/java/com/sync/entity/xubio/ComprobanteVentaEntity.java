package com.sync.entity.xubio;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "xubio_comprobantes_venta")
public class ComprobanteVentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "xubio_id", nullable = false, unique = true)
    private Long xubioId;

    @Column(name = "numero")
    private String numero;

    @Column(name = "punto_de_venta")
    private Integer puntoDeVenta;

    @Column(name = "tipo_de_comprobante")
    private String tipoDeComprobante;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "estado")
    private String estado;

    @Column(name = "condicion_de_pago")
    private String condicionDePago;

    @Column(name = "cliente_xubio_id")
    private Long clienteXubioId;

    @Column(name = "moneda_id")
    private Long monedaId;

    @Column(name = "cotizacion", precision = 18, scale = 6)
    private BigDecimal cotizacion;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "descuento", precision = 18, scale = 2)
    private BigDecimal descuento;

    @Column(name = "total_impuestos", precision = 18, scale = 2)
    private BigDecimal totalImpuestos;

    @Column(name = "total", precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "saldo_pendiente", precision = 18, scale = 2)
    private BigDecimal saldoPendiente;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "importe_mon_principal", precision = 18, scale = 2)
    private BigDecimal importeMonPrincipal;

    @Column(name = "mail_estado")
    private String mailEstado;

    @Column(name = "cbu_informada")
    private Boolean cbuInformada;

    @Column(name = "cae")
    private String cae;

    @Column(name = "cae_fecha_vencimiento")
    private LocalDate caeFechaVencimiento;

    @Column(name = "circuito_contable_id")
    private Long circuitoContableId;

    @Column(name = "deposito_id")
    private Long depositoId;

    @Column(name = "provincia_id")
    private Long provinciaId;

    @Column(name = "porcentaje_comision", precision = 18, scale = 2)
    private BigDecimal porcentajeComision;

    @Column(name = "factura_no_exportacion")
    private Boolean facturaNoExportacion;

    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

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
