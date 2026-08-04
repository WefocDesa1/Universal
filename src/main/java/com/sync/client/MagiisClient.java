package com.sync.client;

import com.sync.dto.magiis.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Cliente Feign para la API de Magiis.
 * Base URL: https://api.public.magiis.com
 *
 * Autenticación:
 *   1. POST /auth/login  → devuelve token (ya incluye "Bearer ")
 *   2. Cada request lleva:
 *      - Header Authorization: <token>
 *      - Header ApplicationName: <username de la app>
 *
 * IMPORTANTE: Todos los endpoints son POST, incluso los de consulta.
 */
@FeignClient(
        name = "magiis-client",
        url = "${magiis.api.base-url}",
        configuration = com.sync.config.FeignConfig.class
)
public interface MagiisClient {

    // ─── AUTH ────────────────────────────────────────────────────────────────

    @PostMapping(value = "/auth/login", consumes = "application/json", produces = "application/json")
    MagiisLoginResponse login(
            @RequestBody MagiisLoginRequest request
    );

    // ─── VIAJES RECIENTES ────────────────────────────────────────────────────
    // No usado por el sync actual (que corre contra Histórico). Se deja
    // disponible por si en el futuro se necesita consultar solo lo reciente.

    @PostMapping(value = "/trip/query/list", consumes = "application/json", produces = "application/json")
    MagiisPagedResponse<MagiisViajeDTO> getViajes(
            @RequestHeader("Authorization") String token,
            @RequestHeader("ApplicationName") String applicationName,
            @RequestBody MagiisViajeQueryRequest request
    );

    // ─── VIAJES HISTÓRICO ────────────────────────────────────────────────────
    // Endpoint que usa el sync (MagiisSyncService.syncViajes).

    @PostMapping(value = "/trip/query/listHistory", consumes = "application/json", produces = "application/json")
    MagiisPagedResponse<MagiisViajeDTO> getViajesHistorico(
            @RequestHeader("Authorization") String token,
            @RequestHeader("ApplicationName") String applicationName,
            @RequestBody MagiisViajeQueryRequest request
    );

    // ─── CUENTAS CORRIENTES ──────────────────────────────────────────────────

    @PostMapping(value = "/client/checking_account_balance/query/list", consumes = "application/json", produces = "application/json")
    MagiisPagedResponse<MagiisCtaCteDTO> getCuentasCorrientes(
            @RequestHeader("Authorization") String token,
            @RequestHeader("ApplicationName") String applicationName,
            @RequestBody MagiisCtaCteQueryRequest request
    );

}
