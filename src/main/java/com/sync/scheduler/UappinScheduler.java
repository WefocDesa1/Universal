package com.sync.scheduler;

import com.sync.service.UappinSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Revisa periódicamente el inbox SFTP de Uappin en busca de reportes nuevos.
 * Hora fijada en Argentina, igual que el resto de los schedulers.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UappinScheduler {

    private static final String ZONE = "America/Argentina/Buenos_Aires";

    private final UappinSyncService uappinSyncService;

    /**
     * Por defecto: todos los días a las 4am (después de que terminan los de Xubio).
     */
    @ConditionalOnProperty(name = "scheduler.uappin.servicios.enabled", havingValue = "true", matchIfMissing = true)
    @Scheduled(cron = "${scheduler.uappin.servicios.cron:0 0 4 * * *}", zone = ZONE)
    public void syncServicios() {
        log.info("[SCHEDULER][UAPPIN] Revisando inbox SFTP de servicios...");
        try {
            var result = uappinSyncService.syncServicios();
            log.info("[SCHEDULER][UAPPIN] Servicios completado: {}", result);
        } catch (Exception e) {
            log.error("[SCHEDULER][UAPPIN] Falló el sync de servicios: {}", e.getMessage());
        }
    }
}
