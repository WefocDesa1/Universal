package com.sync.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sync.client.MagiisClient;
import com.sync.dto.magiis.*;
import com.sync.entity.SyncExecutionLogEntity;
import com.sync.entity.magiis.*;
import com.sync.repository.magiis.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MagiisSyncService {

    private final MagiisClient magiisClient;
    private final MagiisTokenService magiisTokenService;
    private final MagiisViajeRepository viajeRepo;
    private final MagiisCtaCteRepository ctaCteRepo;
    private final SyncLogService syncLogService;
    private final ObjectMapper objectMapper;

    // ─── VIAJES (HISTÓRICO) ──────────────────────────────────────────────────
    // Usa /trip/query/listHistory. El endpoint "Recientes" (getViajes) está
    // disponible en MagiisClient pero no se usa desde este sync.

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncViajes() {
        return syncViajes(null, null);
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncViajes(String desde, String hasta) {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("MAGIIS", "VIAJES");
        int fetched = 0, created = 0, updated = 0;
        int page = 1;

        try {
            String token = magiisTokenService.getToken();
            String appName = magiisTokenService.getApplicationName();

            MagiisPagedResponse<MagiisViajeDTO> response;
            do {
                MagiisViajeQueryRequest request = MagiisViajeQueryRequest.builder()
                        .page(page)
                        .dateFrom(desde)
                        .dateTo(hasta)
                        .build();

                response = magiisClient.getViajesHistorico(token, appName, request);

                List<MagiisViajeDTO> viajes = response.getContent();
                if (viajes == null) break;

                fetched += viajes.size();

                for (MagiisViajeDTO dto : viajes) {
                    var existing = viajeRepo.findByTravelIdForCarrier(dto.getTravelIdForCarrier());
                    if (existing.isPresent()) {
                        updateViaje(existing.get(), dto);
                        updated++;
                    } else {
                        createViaje(dto);
                        created++;
                    }
                }
                page++;
            } while (page <= response.getTotalPages());

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[MAGIIS][VIAJES] Sync completo (histórico): {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[MAGIIS][VIAJES] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createViaje(MagiisViajeDTO dto) {
        MagiisViajeEntity entity = new MagiisViajeEntity();
        mapViajeToEntity(dto, entity);
        viajeRepo.save(entity);
    }

    @Transactional
    protected void updateViaje(MagiisViajeEntity entity, MagiisViajeDTO dto) {
        mapViajeToEntity(dto, entity);
        viajeRepo.save(entity);
    }

    private void mapViajeToEntity(MagiisViajeDTO dto, MagiisViajeEntity entity) {
        entity.setTravelIdForCarrier(dto.getTravelIdForCarrier());
        entity.setState(dto.getState());
        entity.setPassengerFirstName(dto.getPassengerFirstName());
        entity.setPassengerLastName(dto.getPassengerLastName());
        entity.setDriverFirstName(dto.getDriverFirstName());
        entity.setDriverLastName(dto.getDriverLastName());
        entity.setVehicleMark(dto.getVehicleMark());
        entity.setVehicleModel(dto.getVehicleModel());
        entity.setVehicleDomain(dto.getVehicleDomain());
        entity.setCarPlate(dto.getCarPlate());
        entity.setOrigin(dto.getOrigin());
        entity.setDestination(dto.getDestination());
        entity.setCreationDate(dto.getCreationDate());
        entity.setFinalPrice(dto.getFinalPrice());
        entity.setSimulatePrice(dto.getSimulatePrice());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setServiceType(dto.getServiceType());
        entity.setIsProgrammed(dto.getIsProgrammed());
        entity.setDelivery(dto.getDelivery());
        entity.setRawJson(toJson(dto));
    }

    // ─── CUENTAS CORRIENTES ──────────────────────────────────────────────────

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncCuentasCorrientes() {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("MAGIIS", "CTACTE");
        int fetched = 0, created = 0, updated = 0;
        int page = 1;

        try {
            String token = magiisTokenService.getToken();
            String appName = magiisTokenService.getApplicationName();

            MagiisPagedResponse<MagiisCtaCteDTO> response;
            do {
                MagiisCtaCteQueryRequest request = MagiisCtaCteQueryRequest.builder()
                        .page(page)
                        .build();

                response = magiisClient.getCuentasCorrientes(token, appName, request);

                List<MagiisCtaCteDTO> items = response.getContent();
                if (items == null) break;

                fetched += items.size();

                for (MagiisCtaCteDTO dto : items) {
                    var existing = ctaCteRepo.findByCheckingAccountId(dto.getCheckingAccountId());
                    if (existing.isPresent()) {
                        updateCtaCte(existing.get(), dto);
                        updated++;
                    } else {
                        createCtaCte(dto);
                        created++;
                    }
                }
                page++;
            } while (page <= response.getTotalPages());

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[MAGIIS][CTACTE] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[MAGIIS][CTACTE] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createCtaCte(MagiisCtaCteDTO dto) {
        MagiisCtaCteEntity entity = new MagiisCtaCteEntity();
        mapCtaCteToEntity(dto, entity);
        ctaCteRepo.save(entity);
    }

    @Transactional
    protected void updateCtaCte(MagiisCtaCteEntity entity, MagiisCtaCteDTO dto) {
        mapCtaCteToEntity(dto, entity);
        ctaCteRepo.save(entity);
    }

    private void mapCtaCteToEntity(MagiisCtaCteDTO dto, MagiisCtaCteEntity entity) {
        entity.setCheckingAccountId(dto.getCheckingAccountId());
        entity.setName(dto.getName());
        entity.setPassengerClass(dto.getPassengerClass());
        entity.setBalance(dto.getBalance());
        entity.setLimitAmount(dto.getLimit());
        entity.setRawJson(toJson(dto));
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public record SyncResult(int fetched, int created, int updated) {}
}
