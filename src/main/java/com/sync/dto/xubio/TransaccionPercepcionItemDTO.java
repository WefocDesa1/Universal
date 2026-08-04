package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para los items de percepciones en una transacción.
 * Incluye impuestos percibidos (ej: retenciones de IVA, Ganancias, etc).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionPercepcionItemDTO {

    @JsonProperty("transaccionCVItemId")
    private Long transaccionCVItemId;

    @JsonProperty("percepcionImpuesto")
    private EntidadRefDTO percepcionImpuesto;

    @JsonProperty("transaccionId")
    private Long transaccionId;

    @JsonProperty("centroDeCosto")
    private EntidadRefDTO centroDeCosto;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("importe")
    private BigDecimal importe;
}
