package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para los instrumentos de pago dentro de un pago a proveedor.
 * Corresponde al array "transaccionInstrumentoDePago" en el JSON de /pagoBean.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionInstrumentoDePagoDTO {

    @JsonProperty("transaccionIPId")
    private Long transaccionIPId;

    @JsonProperty("tipoCuenta")
    private Integer tipoCuenta;

    @JsonProperty("cuenta")
    private EntidadRefDTO cuenta;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;

    @JsonProperty("cotizacion")
    private BigDecimal cotizacion;

    @JsonProperty("importe")
    private BigDecimal importe;

    @JsonProperty("chequeTerceros")
    private Integer chequeTerceros;

    @JsonProperty("chequePropio")
    private String chequePropio;

    @JsonProperty("vencimientoCheque")
    private LocalDate vencimientoCheque;

    @JsonProperty("banco")
    private EntidadRefDTO banco;

    @JsonProperty("descripcion")
    private String descripcion;
}
