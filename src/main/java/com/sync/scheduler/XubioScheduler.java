package com.sync.scheduler;

import com.sync.service.XubioSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Scheduler para sincronizar datos de Xubio.
 *
 * Cada tarea se puede activar/desactivar individualmente con la propiedad
 * scheduler.xubio.<entidad>.enabled en application.yml. Ese chequeo se hace
 * a mano al principio de cada método (con un campo @Value) en vez de con
 * @ConditionalOnProperty, porque esa anotación puesta sobre un método
 * @Scheduled no tiene ningún efecto: Spring solo evalúa condiciones al
 * registrar beans/clases, no método por método. Con una sola clase exponiendo
 * varias tareas independientes, no alcanza con gatear a nivel de clase.
 *
 * Los endpoints con filtro de fecha (comprobantes, cobranzas, pagos) sincronizan
 * una única vez por día la ventana de los últimos {@value #WINDOW_DAYS} días:
 * cubre tanto lo del día (últimas 24hs) como correcciones/altas tardías de días
 * previos, sin duplicar requests contra la API de Xubio. Los horarios de cada
 * tarea están escalonados entre sí para no golpear la API con varios requests
 * en simultáneo y evitar baneos.
 *
 * Todos los horarios están fijados en hora de Argentina ({@link #ZONE}), sin
 * importar el timezone del servidor/contenedor donde corra la app (en Railway,
 * por ejemplo, el default es UTC).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XubioScheduler {

    private static final int WINDOW_DAYS = 4;
    private static final String ZONE = "America/Argentina/Buenos_Aires";

    private final XubioSyncService xubioSyncService;

    @Value("${scheduler.xubio.clientes.enabled:true}")
    private boolean clientesEnabled;

    @Value("${scheduler.xubio.proveedores.enabled:true}")
    private boolean proveedoresEnabled;

    @Value("${scheduler.xubio.comprobanteVenta.enabled:true}")
    private boolean comprobanteVentaEnabled;

    @Value("${scheduler.xubio.comprobanteCompra.enabled:true}")
    private boolean comprobanteCompraEnabled;

    @Value("${scheduler.xubio.cobranzas.enabled:true}")
    private boolean cobranzasEnabled;

    @Value("${scheduler.xubio.pagos.enabled:true}")
    private boolean pagosEnabled;

    /**
     * Sincroniza clientes de Xubio. Sin filtro de fecha: siempre trae el listado completo.
     * Por defecto: todos los días a las 2am.
     */
    @Scheduled(cron = "${scheduler.xubio.clientes.cron:0 0 2 * * *}", zone = ZONE)
    public void syncClientes() {
        if (!clientesEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de clientes deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de clientes...");
        try {
            var result = xubioSyncService.syncClientes();
            log.info("[SCHEDULER][XUBIO] Clientes completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de clientes: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza proveedores de Xubio. Sin filtro de fecha: siempre trae el listado completo.
     * Por defecto: todos los días a la 1:30am.
     */
    @Scheduled(cron = "${scheduler.xubio.proveedores.cron:0 30 1 * * *}", zone = ZONE)
    public void syncProveedores() {
        if (!proveedoresEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de proveedores deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de proveedores...");
        try {
            var result = xubioSyncService.syncProveedor();
            log.info("[SCHEDULER][XUBIO] Proveedores completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de proveedores: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Comprobantes de Venta de los últimos {@value #WINDOW_DAYS} días.
     * Por defecto: todos los días a las 2:15am.
     */
    @Scheduled(cron = "${scheduler.xubio.comprobanteVenta.cron:0 15 2 * * *}", zone = ZONE)
    public void syncComprobanteVenta() {
        if (!comprobanteVentaEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de comprobantes de venta deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de comprobantes de venta (últimos {} días)...", WINDOW_DAYS);
        try {
            var result = xubioSyncService.syncComprobanteVenta(windowStart(), windowEnd());
            log.info("[SCHEDULER][XUBIO] Comprobantes de venta completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de comprobantes de venta: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Comprobantes de Compra de los últimos {@value #WINDOW_DAYS} días.
     * Por defecto: todos los días a las 2:30am.
     */
    @Scheduled(cron = "${scheduler.xubio.comprobanteCompra.cron:0 30 2 * * *}", zone = ZONE)
    public void syncComprobanteCompra() {
        if (!comprobanteCompraEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de comprobantes de compra deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de comprobantes de compra (últimos {} días)...", WINDOW_DAYS);
        try {
            var result = xubioSyncService.syncComprobanteCompra(windowStart(), windowEnd());
            log.info("[SCHEDULER][XUBIO] Comprobantes de compra completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de comprobantes de compra: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Cobranzas de los últimos {@value #WINDOW_DAYS} días.
     * Por defecto: todos los días a las 2:45am.
     */
    @Scheduled(cron = "${scheduler.xubio.cobranzas.cron:0 45 2 * * *}", zone = ZONE)
    public void syncCobranzas() {
        if (!cobranzasEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de cobranzas deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de cobranzas (últimos {} días)...", WINDOW_DAYS);
        try {
            var result = xubioSyncService.syncCobranzas(windowStart(), windowEnd());
            log.info("[SCHEDULER][XUBIO] Cobranzas completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de cobranzas: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza Pagos de los últimos {@value #WINDOW_DAYS} días.
     * Por defecto: todos los días a las 3:00am.
     */
    @Scheduled(cron = "${scheduler.xubio.pagos.cron:0 0 3 * * *}", zone = ZONE)
    public void syncPagos() {
        if (!pagosEnabled) {
            log.debug("[SCHEDULER][XUBIO] Sync de pagos deshabilitado, se omite");
            return;
        }
        log.info("[SCHEDULER][XUBIO] Iniciando sync de pagos (últimos {} días)...", WINDOW_DAYS);
        try {
            var result = xubioSyncService.syncPagos(windowStart(), windowEnd());
            log.info("[SCHEDULER][XUBIO] Pagos completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][XUBIO] Falló el sync de pagos: {}", e.getMessage());
        }
    }

    private String windowStart() {
        return LocalDate.now(ZoneId.of(ZONE)).minusDays(WINDOW_DAYS - 1).toString();
    }

    private String windowEnd() {
        return LocalDate.now(ZoneId.of(ZONE)).toString();
    }
}
