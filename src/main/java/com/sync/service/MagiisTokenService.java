package com.sync.service;

import com.sync.client.MagiisClient;
import com.sync.config.MagiisProperties;
import com.sync.dto.magiis.MagiisLoginRequest;
import com.sync.dto.magiis.MagiisLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Gestiona el ciclo de vida del token de Magiis.
 *
 * Flujo:
 *   POST /auth/login { username, password }
 *   Header: RoleToAttempt = "" (vacío)
 *   Response: { "token": "Bearer eyJ..." }
 *
 * El token devuelto ya incluye el prefijo "Bearer ",
 * se usa directamente en el header Authorization de los requests siguientes.
 *
 * La API no especifica tiempo de expiración en la respuesta,
 * se renueva cada 50 minutos por seguridad.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MagiisTokenService {

    private static final long TOKEN_TTL_SECONDS = 50 * 60; // 50 minutos

    private final MagiisClient magiisClient;
    private final MagiisProperties properties;

    private String currentToken;
    private Instant tokenExpiration;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public synchronized String getToken() {
        if (isTokenValid()) {
            return currentToken;
        }
        log.info("Iniciando sesión en Magiis para usuario '{}'...", properties.getUsername());

        MagiisLoginRequest request = new MagiisLoginRequest(
                properties.getUsername(),
                properties.getPassword()
        );

        MagiisLoginResponse response = magiisClient.login(request);

        // El token ya viene con "Bearer " incluido
        currentToken = response.getToken();
        tokenExpiration = Instant.now().plusSeconds(TOKEN_TTL_SECONDS);

        log.info("Sesión Magiis iniciada correctamente.");
        return currentToken;
    }

    public String getApplicationName() {
        return properties.getUsername();
    }

    private boolean isTokenValid() {
        return currentToken != null
                && tokenExpiration != null
                && Instant.now().isBefore(tokenExpiration);
    }

    public void invalidateToken() {
        currentToken = null;
        tokenExpiration = null;
    }
}
