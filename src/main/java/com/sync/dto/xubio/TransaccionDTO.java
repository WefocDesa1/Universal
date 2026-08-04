package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para transacciones contables de Xubio.
 * Incluye compras, ventas, órdenes de pago, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionDTO {

    @JsonProperty("circuitoContable")
    private EntidadRefDTO circuitoContable;

    @JsonProperty("comprobante")
    private Integer comprobante;

    @JsonProperty("fechaFiscal")
    private LocalDate fechaFiscal;

    @JsonProperty("cuentaBancaria")
    private EntidadRefDTO cuentaBancaria;

    @JsonProperty("transaccionid")
    private Long transaccionId;

    @JsonProperty("externalId")
    private String externalId;

    @JsonProperty("tipo")
    private Integer tipo;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("fecha")
    private LocalDate fecha;

    @JsonProperty("fechaVto")
    private LocalDate fechaVto;

    @JsonProperty("puntoVenta")
    private String puntoVenta;

    @JsonProperty("numeroDocumento")
    private String numeroDocumento;

    @JsonProperty("condicionDePago")
    private Integer condicionDePago;

    @JsonProperty("deposito")
    private EntidadRefDTO deposito;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("importeMonPrincipal")
    private BigDecimal importeMonPrincipal;

    @JsonProperty("importetotal")
    private BigDecimal importeTotal;

    @JsonProperty("importeImpuestos")
    private BigDecimal importeImpuestos;

    @JsonProperty("importeGravado")
    private BigDecimal importeGravado;

    @JsonProperty("provincia")
    private ProvinciaDTO provincia;

    @JsonProperty("cotizacionListaDePrecio")
    private BigDecimal cotizacionListaDePrecio;

    @JsonProperty("listaDePrecio")
    private EntidadRefDTO listaDePrecio;

    @JsonProperty("fechaComprobante")
    private LocalDate fechaComprobante;

    @JsonProperty("proveedor")
    private EntidadRefDTO proveedor;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("cbuinformada")
    private Boolean cbuinformada;

    @JsonProperty("transaccionProductoItems")
    private List<TransaccionProductoItemDTO> transaccionProductoItems;

    @JsonProperty("transaccionPercepcionItems")
    private List<TransaccionPercepcionItemDTO> transaccionPercepcionItems;

    @JsonProperty("transaccionOrdenPagoItems")
    private List<TransaccionOrdenPagoItemDTO> transaccionOrdenPagoItems;
}
