package com.sync.dto.magiis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagiisLoginResponse {
    private String token;
}
