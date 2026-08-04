package com.sync.service;

import com.sync.client.XubioClient;
import com.sync.config.XubioProperties;

import com.sync.dto.xubio.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

/**
 * Gestiona el ciclo de vida del token OAuth2 de Xubio.
 * Renueva automáticamente antes de la expiración.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class XubioTokenService {

    private final XubioClient xubioClient;
    private final XubioProperties properties;

    private String currentToken;
    private Instant tokenExpiration;

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public synchronized String getBearerToken() {
        if (isTokenValid()) {
            return "Bearer " + currentToken;
        }
        log.info("Solicitando nuevo token Xubio...");
        
        String auth = properties.getClientId() + ":" + properties.getClientSecret();
        String basicAuth = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());

        TokenResponse response = xubioClient.getToken(basicAuth, Map.of("grant_type", "client_credentials"));
        currentToken = response.getAccessToken();
        // Renovar 60 segundos antes de que expire
        tokenExpiration = Instant.now().plusSeconds(response.getExpiresIn() - 60);

        log.info("Token Xubio obtenido, expira en {} segundos", response.getExpiresIn());
        return "Bearer " + currentToken;
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
