package com.sync.scheduler;

import com.sync.service.XubioSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para sincronizar datos de Xubio.
 *
 * Cada tarea se puede activar/desactivar individualmente
 * con la propiedad scheduler.xubio.<entidad>.enabled en application.yml
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XubioScheduler {

    private final XubioSyncService xubioSyncService;

    /**
     * Sincroniza clientes de Xubio.
     * Por defecto: todos los días a las 2am.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.clientes.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.clientes.cron:0 0 2 * * *}")
    public void syncClientes() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de clientes...");
        try {
            var result = xubioSyncService.syncClientes();
            log.info("[SCHEDULER][XUBIO] Clientes completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de clientes: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza proveedores de Xubio.
     * Por defecto: todos los días a la 1:30am.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.proveedores.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.proveedores.cron:0 30 1 * * *}")
    public void syncProveedores() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de proveedores...");
        try {
            var result = xubioSyncService.syncProveedor();
            log.info("[SCHEDULER][XUBIO] Proveedores completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de productos: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Comprobantes de Venta.
     * Por defecto: cada hora.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.comprobanteVenta.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.comprobanteVenta.cron:0 0 * * * *}")
    public void syncComprobanteVenta() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de comprobantes de venta...");
        try {
            var result = xubioSyncService.syncComprobanteVenta();
            log.info("[SCHEDULER][XUBIO] Comprobantes de venta completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de comprobantes de venta: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Comprobantes de Compra.
     * Por defecto: cada hora a los :15.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.comprobanteCompra.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.comprobanteCompra.cron:0 15 * * * *}")
    public void syncComprobanteCompra() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de comprobantes de compra...");
        try {
            var result = xubioSyncService.syncComprobanteCompra();
            log.info("[SCHEDULER][XUBIO] Comprobantes de compra completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de comprobantes de compra: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Cobranzas.
     * Por defecto: cada hora a los :30.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.cobranzas.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.cobranzas.cron:0 30 * * * *}")
    public void syncCobranzas() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de cobranzas...");
        try {
            var result = xubioSyncService.syncCobranzas();
            log.info("[SCHEDULER][XUBIO] Cobranzas completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de cobranzas: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Pagos.
     * Por defecto: cada hora a los :45.
     */
    @ConditionalOnProperty(name = "scheduler.xubio.pagos.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.xubio.pagos.cron:0 45 * * * *}")
    public void syncPagos() {
        log.info("[SCHEDULER][XUBIO] Iniciando sync de pagos...");
        try {
            var result = xubioSyncService.syncPagos();
            log.info("[SCHEDULER][XUBIO] Pagos completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de pagos: {}", e.getMessage());
        }
    }
}
