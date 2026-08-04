package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para los items de productos en una transacción.
 * Representa cada línea de compra/venta en el comprobante.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionProductoItemDTO {

    @JsonProperty("transaccionCVItemId")
    private Long transaccionCVItemId;

    @JsonProperty("precioconivaincluido")
    private BigDecimal precioConIvaIncluido;

    @JsonProperty("transaccionId")
    private Long transaccionId;

    @JsonProperty("producto")
    private EntidadRefDTO producto;

    @JsonProperty("centroDeCosto")
    private EntidadRefDTO centroDeCosto;

    @JsonProperty("deposito")
    private EntidadRefDTO deposito;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("cantidad")
    private BigDecimal cantidad;

    @JsonProperty("precio")
    private BigDecimal precio;

    @JsonProperty("iva")
    private BigDecimal iva;

    @JsonProperty("importe")
    private BigDecimal importe;

    @JsonProperty("total")
    private BigDecimal total;

    @JsonProperty("montoExento")
    private BigDecimal montoExento;

    @JsonProperty("porcentajeDescuento")
    private BigDecimal porcentajeDescuento;
}
