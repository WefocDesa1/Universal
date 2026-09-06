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
 *
 * El @ConditionalOnProperty va a nivel de clase (no del método @Scheduled):
 * puesto sobre el método no tiene ningún efecto, porque Spring solo evalúa
 * condiciones al registrar beans/clases, no método por método. Como esta
 * clase tiene un único scheduled task, gatearla a nivel de clase alcanza.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "scheduler.uappin.servicios.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class UappinScheduler {

    private static final String ZONE = "America/Argentina/Buenos_Aires";

    private final UappinSyncService uappinSyncService;

    /**
     * Por defecto: todos los días a las 4am (después de que terminan los de Xubio).
     */
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
