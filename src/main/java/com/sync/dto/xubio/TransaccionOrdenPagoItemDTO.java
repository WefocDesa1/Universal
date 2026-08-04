package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para los items de orden de pago en una transacción.
 * Detalla los pagos: banco, cheque, moneda, cotización, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionOrdenPagoItemDTO {

    @JsonProperty("transaccionid")
    private Long transaccionId;

    @JsonProperty("itemId")
    private Long itemId;

    @JsonProperty("cuentaTipo")
    private String cuentaTipo;

    @JsonProperty("cuentaId")
    private Long cuentaId;

    @JsonProperty("moneda")
    private EntidadRefDTO moneda;

    @JsonProperty("cotizacionMonTransaccion")
    private BigDecimal cotizacionMonTransaccion;

    @JsonProperty("importeMonPrincipal")
    private BigDecimal importeMonPrincipal;

    @JsonProperty("importeMonTransaccion")
    private BigDecimal importeMonTransaccion;

    @JsonProperty("chequeTerceros")
    private String chequeTerceros;

    @JsonProperty("chequePropios")
    private String chequePropios;

    @JsonProperty("fechaVto")
    private LocalDate fechaVto;

    @JsonProperty("banco")
    private EntidadRefDTO banco;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("transaccionId")
    private Long transaccionIdVerify;
}
