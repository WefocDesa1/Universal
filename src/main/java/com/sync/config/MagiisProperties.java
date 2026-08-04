package com.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "magiis.api")
public class MagiisProperties {
    private String baseUrl;
    private String username;
    private String password;
    private int connectTimeout = 5000;
    private int readTimeout = 15000;
}
