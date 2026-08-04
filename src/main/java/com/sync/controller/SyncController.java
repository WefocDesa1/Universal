package com.sync.controller;

import com.sync.service.MagiisSyncService;
import com.sync.service.XubioSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * API REST para disparar syncs manualmente y consultar estado.
 *
 * Útil para: forzar una sincronización sin esperar al scheduler,
 * debugging, integración con CI/CD pipelines.
 */
@Slf4j
@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private final XubioSyncService xubioSyncService;
    private final MagiisSyncService magiisSyncService;

    // ─── XUBIO ───────────────────────────────────────────────────────────────

    @PostMapping("/xubio/clientes")
    public ResponseEntity<Map<String, Object>> syncXubioClientes() {
        log.info("Sync manual: Xubio Clientes");
        var result = xubioSyncService.syncClientes();
        return ResponseEntity.ok(toMap("xubio", "clientes", result));
    }

    @PostMapping("/xubio/proveedores")
    public ResponseEntity<Map<String, Object>> syncXubioProveedores() {
        log.info("Sync manual: Xubio Proveedores");
        var result = xubioSyncService.syncProveedor();
        return ResponseEntity.ok(toMap("xubio", "proveedores", result));
    }

    @PostMapping("/xubio/comprobante-venta")
    public ResponseEntity<Map<String, Object>> syncXubioComprobanteVenta(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        log.info("Sync manual: Xubio Comprobante de Venta");
        var result = xubioSyncService.syncComprobanteVenta(desde, hasta);
        return ResponseEntity.ok(toMap("xubio", "comprobante_venta", result));
    }

    @PostMapping("/xubio/comprobante-compra")
    public ResponseEntity<Map<String, Object>> syncXubioComprobanteCompra(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        log.info("Sync manual: Xubio Comprobante de Compra");
        var result = xubioSyncService.syncComprobanteCompra(desde, hasta);
        return ResponseEntity.ok(toMap("xubio", "comprobante_compra", result));
    }

    @PostMapping("/xubio/cobranzas")
    public ResponseEntity<Map<String, Object>> syncXubioCobranzas(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        log.info("Sync manual: Xubio Cobranzas");
        var result = xubioSyncService.syncCobranzas(desde, hasta);
        return ResponseEntity.ok(toMap("xubio", "cobranzas", result));
    }

    @PostMapping("/xubio/pagos")
    public ResponseEntity<Map<String, Object>> syncXubioPagos(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        log.info("Sync manual: Xubio Pagos");
        var result = xubioSyncService.syncPagos(desde, hasta);
        return ResponseEntity.ok(toMap("xubio", "pagos", result));
    }

    // ─── MAGIIS ──────────────────────────────────────────────────────────────

    @PostMapping("/magiis/viajes")
    public ResponseEntity<Map<String, Object>> syncMagiisViajes(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        log.info("Sync manual: Magiis Viajes (histórico)");
        var result = magiisSyncService.syncViajes(desde, hasta);
        return ResponseEntity.ok(toMap("magiis", "viajes", result));
    }

    @PostMapping("/magiis/cuentas-corrientes")
    public ResponseEntity<Map<String, Object>> syncMagiisCuentasCorrientes() {
        log.info("Sync manual: Magiis Cuentas Corrientes");
        var result = magiisSyncService.syncCuentasCorrientes();
        return ResponseEntity.ok(toMap("magiis", "cuentas_corrientes", result));
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private Map<String, Object> toMap(String source, String entity, XubioSyncService.SyncResult result) {
        return Map.of(
                "source", source,
                "entity", entity,
                "fetched", result.fetched(),
                "created", result.created(),
                "updated", result.updated(),
                "status", "success"
        );
    }

    private Map<String, Object> toMap(String source, String entity, MagiisSyncService.SyncResult result) {
        return Map.of(
                "source", source,
                "entity", entity,
                "fetched", result.fetched(),
                "created", result.created(),
                "updated", result.updated(),
                "status", "success"
        );
    }
}
