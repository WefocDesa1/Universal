package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para Comprobantes de Compra (facturas recibidas de proveedores).
 * Endpoint: GET /comprobanteCompraBean
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComprobanteCompraDTO {

    @JsonProperty("transaccionid")
    private Long comprobanteCompraId;

    @JsonProperty("numeroDocumento")
    private String numero;

    @JsonProperty("puntoVenta")
    private EntidadRefDTO puntoDeVenta;

    @JsonProperty("tipo")
    private String tipoDeComprobante;   // 1 para FC de Compra, etc.

    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("fechaVto")
    private LocalDate fechaVencimiento;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("condicionDePago")
    private String condicionDePago;

    @JsonProperty("proveedor")
    private EntidadRefDTO proveedor;

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

    @JsonProperty("circuitoContable")
    private EntidadRefDTO circuitoContable;

    @JsonProperty("fechaComprobante")
    private LocalDate fechaComprobante;

    @JsonProperty("fechaFiscal")
    private LocalDate fechaFiscal;

    @JsonProperty("cbuinformada")
    private Boolean cbuInformada;
}
