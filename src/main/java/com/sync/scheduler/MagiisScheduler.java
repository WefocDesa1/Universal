package com.sync.scheduler;

import com.sync.service.MagiisSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para sincronizar datos de Magiis.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MagiisScheduler {

    private final MagiisSyncService magiisSyncService;

    /**
     * Sincroniza viajes de Magiis (histórico).
     * Por defecto: cada hora a los 15 minutos.
     */
    @ConditionalOnProperty(name = "scheduler.magiis.viajes.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.magiis.viajes.cron:0 15 * * * *}")
    public void syncViajes() {
        log.info("[SCHEDULER][MAGIIS] Iniciando sync de viajes (histórico)...");
        try {
            var result = magiisSyncService.syncViajes();
            log.info("[SCHEDULER][MAGIIS] Viajes completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][MAGIIS] Falló el sync de viajes: {}", e.getMessage());
        }
    }

    /**
     * Sincroniza cuentas corrientes de Magiis.
     * Por defecto: todos los días a las 3am.
     */
    @ConditionalOnProperty(name = "scheduler.magiis.cuentasCorrientes.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.magiis.cuentasCorrientes.cron:0 0 3 * * *}")
    public void syncCuentasCorrientes() {
        log.info("[SCHEDULER][MAGIIS] Iniciando sync de cuentas corrientes...");
        try {
            var result = magiisSyncService.syncCuentasCorrientes();
            log.info("[SCHEDULER][MAGIIS] Cuentas corrientes completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][MAGIIS] Falló el sync de cuentas corrientes: {}", e.getMessage());
        }
    }
}
