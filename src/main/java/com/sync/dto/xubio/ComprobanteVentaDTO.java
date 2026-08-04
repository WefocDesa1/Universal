package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para Comprobantes de Venta (facturas emitidas a clientes).
 * Endpoint: GET /comprobanteVentaBean
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComprobanteVentaDTO {

    @JsonProperty("transaccionid")
    private Long comprobanteVentaId;

    @JsonProperty("numeroDocumento")
    private String numero;

    @JsonProperty("puntoVenta")
    private EntidadRefDTO puntoDeVenta;

    @JsonProperty("tipo")
    private String tipoDeComprobante;   // 3 (String mapping for numbers works in Jackson)

    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("fechaVto")
    private LocalDate fechaVencimiento;

    @JsonProperty("estado")
    private String estado;              // Podría ser mailEstado o estado real del comprobante

    @JsonProperty("condicionDePago")
    private String condicionDePago;

    @JsonProperty("cliente")
    private EntidadRefDTO cliente;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("importeGravado")
    private BigDecimal subtotal;

    @JsonProperty("descuento")
    private BigDecimal descuento;

    @JsonProperty("importeImpuestos")
    private BigDecimal totalImpuestos;

    @JsonProperty("importetotal")
    private BigDecimal total;

    @JsonProperty("saldoPendiente")
    private BigDecimal saldoPendiente;

    @JsonProperty("descripcion")
    private String observaciones;

    // --- Nuevos campos extraídos del JSON ---
    @JsonProperty("importeMonPrincipal")
    private BigDecimal importeMonPrincipal;

    @JsonProperty("mailEstado")
    private String mailEstado;

    @JsonProperty("cbuinformada")
    private Boolean cbuinformada;

    @JsonProperty("cae")
    private String cae;

    @JsonProperty("CAE")
    private String caeUpper;

    @JsonProperty("circuitoContable")
    private EntidadRefDTO circuitoContable;

    @JsonProperty("deposito")
    private EntidadRefDTO deposito;

    @JsonProperty("provincia")
    private EntidadRefDTO provincia;

    @JsonProperty("porcentajeComision")
    private BigDecimal porcentajeComision;

    @JsonProperty("facturaNoExportacion")
    private Boolean facturaNoExportacion;

    @JsonProperty("caefechaVto")
    private LocalDate caeFechaVencimiento;
}
