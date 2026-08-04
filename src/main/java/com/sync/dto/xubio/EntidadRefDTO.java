package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para referencias simples a entidades.
 * Se reutiliza para: circuitoContable, cuentaBancaria, deposito, listaDePrecio,
 * proveedor, producto, centroDeCosto, percepcionImpuesto, moneda, banco, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntidadRefDTO {

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("id")
    private Long id;
}
