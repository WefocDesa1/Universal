package com.sync.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "uappin.sftp")
public class UappinSftpProperties {
    private String host;
    private int port = 22;
    private String username;
    /** Clave privada en formato PEM (contenido completo, no un path). */
    private String privateKey;
    private String inboxPath = "/upload/inbox";
    private String procesadosPath = "/upload/procesados";
    private String errorPath = "/upload/error";
    private int connectTimeout = 10000;
}
