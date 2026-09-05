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
    /** Clave privada en formato PEM (contenido completo, no un path). Preferida por sobre password si ambas están seteadas. */
    private String privateKey;
    /** Alternativa a la clave, más simple de levantar en un SFTP de prueba (todo por env var, sin montar archivos). */
    private String password;
    private String inboxPath = "/upload/inbox";
    private String procesadosPath = "/upload/procesados";
    private String errorPath = "/upload/error";
    private int connectTimeout = 10000;
}
