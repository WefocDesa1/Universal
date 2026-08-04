package com.sync.dto.xubio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProveedorDTO {

    @JsonProperty("proveedorid")
    private String proveedorId;

    @JsonProperty("nombre")
    private String nombre;

}
