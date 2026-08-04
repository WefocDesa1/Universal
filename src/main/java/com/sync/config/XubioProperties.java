package com.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "xubio.api")
public class XubioProperties {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String tokenUrl;
    private int connectTimeout = 5000;
    private int readTimeout = 15000;
}
