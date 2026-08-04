package com.sync.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sync.client.XubioClient;
import com.sync.dto.xubio.*;
import com.sync.entity.SyncExecutionLogEntity;
import com.sync.entity.xubio.*;
import com.sync.repository.xubio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class XubioSyncService {

    private final XubioClient xubioClient;
    private final XubioTokenService tokenService;
    private final XubioClienteRepository clienteRepo;
    private final XubioProveedorRepository proveedorRepo;
    private final ComprobanteVentaRepository comprobanteVentaRepo;
    private final ComprobanteCompraRepository comprobanteCompraRepo;
    private final CobranzaRepository cobranzaRepo;
    private final PagoRepository pagoRepo;
    private final MonedaRepository monedaRepo;
    private final CircuitoContableRepository circuitoContableRepo;
    private final DepositoRepository depositoRepo;
    private final PuntoVentaRepository puntoVentaRepo;
    private final ProvinciaRepository provinciaRepo;
    private final BancoRepository bancoRepo;
    private final CuentaBancariaRepository cuentaBancariaRepo;
    private final ConceptoRetencionRepository conceptoRetencionRepo;
    private final SyncLogService syncLogService;
    private final ObjectMapper objectMapper;

    // ─── CLIENTES ────────────────────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncClientes() {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "CLIENTES");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<ClienteDTO> clientes = xubioClient.getClientes(tokenService.getBearerToken());
            fetched += clientes.size();

            for (ClienteDTO dto : clientes) {
                var existing = clienteRepo.findByXubioId(dto.getClienteId());
                if (existing.isPresent()) {
                    updateCliente(existing.get(), dto);
                    updated++;
                } else {
                    createCliente(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][CLIENTES] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][CLIENTES] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createCliente(ClienteDTO dto) {
        XubioClienteEntity entity = new XubioClienteEntity();
        mapClienteToEntity(dto, entity);
        clienteRepo.save(entity);
    }

    @Transactional
    protected void updateCliente(XubioClienteEntity entity, ClienteDTO dto) {
        mapClienteToEntity(dto, entity);
        clienteRepo.save(entity);
    }

    private void mapClienteToEntity(ClienteDTO dto, XubioClienteEntity entity) {
        entity.setXubioId(dto.getClienteId());
        entity.setClienteId(dto.getClienteId());
        entity.setNombre(dto.getNombre());
        entity.setRawJson(toJson(dto));
    }

    // ─── PROVEEDOR ────────────────────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncProveedor() {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "PROVEEDORES");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<ProveedorDTO> proveedores = xubioClient.getProveedores(tokenService.getBearerToken());
            fetched += proveedores.size();

            for (ProveedorDTO dto : proveedores) {
                var existing = proveedorRepo.findByXubioId(dto.getProveedorId());
                if (existing.isPresent()) {
                    updateProveedor(existing.get(), dto);
                    updated++;
                } else {
                    createProveedor(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][PROVEEDORES] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][PROVEEDORES] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createProveedor(ProveedorDTO dto) {
        XubioProveedorEntity entity = new XubioProveedorEntity();
        mapProveedorToEntity(dto, entity);
        proveedorRepo.save(entity);
    }

    @Transactional
    protected void updateProveedor(XubioProveedorEntity entity, ProveedorDTO dto) {
        mapProveedorToEntity(dto, entity);
        proveedorRepo.save(entity);
    }

    private void mapProveedorToEntity(ProveedorDTO dto, XubioProveedorEntity entity) {
        entity.setXubioId(dto.getProveedorId());
        entity.setProveedorId(dto.getProveedorId());
        entity.setNombre(dto.getNombre());
        entity.setRawJson(toJson(dto));
    }

    // ─── COMPROBANTES DE VENTA ───────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncComprobanteVenta() {
        return syncComprobanteVenta(null, null);
    }

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncComprobanteVenta(String fechaDesde, String fechaHasta) {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "COMPROBANTE_VENTA");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<ComprobanteVentaDTO> items =
                    xubioClient.getComprobanteVenta(tokenService.getBearerToken(), fechaDesde, fechaHasta);
            fetched = items.size();

            for (ComprobanteVentaDTO dto : items) {
                var existing = comprobanteVentaRepo.findByXubioId(dto.getComprobanteVentaId());
                if (existing.isPresent()) {
                    updateComprobanteVenta(existing.get(), dto);
                    updated++;
                } else {
                    createComprobanteVenta(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][COMPROBANTE_VENTA] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][COMPROBANTE_VENTA] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createComprobanteVenta(ComprobanteVentaDTO dto) {
        ComprobanteVentaEntity entity = new ComprobanteVentaEntity();
        mapComprobanteVentaToEntity(dto, entity);
        comprobanteVentaRepo.save(entity);
    }

    @Transactional
    protected void updateComprobanteVenta(ComprobanteVentaEntity entity, ComprobanteVentaDTO dto) {
        mapComprobanteVentaToEntity(dto, entity);
        comprobanteVentaRepo.save(entity);
    }

    private void mapComprobanteVentaToEntity(ComprobanteVentaDTO dto, ComprobanteVentaEntity entity) {
        entity.setXubioId(dto.getComprobanteVentaId());
        entity.setNumero(dto.getNumero());
        entity.setPuntoDeVenta(dto.getPuntoDeVenta() != null && dto.getPuntoDeVenta().getId() != null ? dto.getPuntoDeVenta().getId().intValue() : null);
        entity.setTipoDeComprobante(dto.getTipoDeComprobante());
        entity.setFecha(dto.getFecha());
        entity.setFechaVencimiento(dto.getFechaVencimiento());
        entity.setEstado(dto.getEstado());
        entity.setCondicionDePago(dto.getCondicionDePago());
        entity.setClienteXubioId(dto.getCliente() != null ? dto.getCliente().getId() : null);
        entity.setMonedaId(dto.getMoneda() != null ? dto.getMoneda().getId() : null);
        entity.setCotizacion(dto.getCotizacion());
        entity.setSubtotal(dto.getSubtotal());
        entity.setDescuento(dto.getDescuento());
        entity.setTotalImpuestos(dto.getTotalImpuestos());
        entity.setTotal(dto.getTotal());
        entity.setSaldoPendiente(dto.getSaldoPendiente());
        entity.setObservaciones(dto.getObservaciones());
        entity.setImporteMonPrincipal(dto.getImporteMonPrincipal());
        entity.setMailEstado(dto.getMailEstado());
        entity.setCbuInformada(dto.getCbuinformada());
        entity.setCae(dto.getCae());
        entity.setCaeFechaVencimiento(dto.getCaeFechaVencimiento());
        entity.setCircuitoContableId(dto.getCircuitoContable() != null ? dto.getCircuitoContable().getId() : null);
        entity.setDepositoId(dto.getDeposito() != null ? dto.getDeposito().getId() : null);
        entity.setProvinciaId(dto.getProvincia() != null ? dto.getProvincia().getId() : null);
        entity.setPorcentajeComision(dto.getPorcentajeComision());
        entity.setFacturaNoExportacion(dto.getFacturaNoExportacion());
        entity.setRawJson(toJson(dto));

        upsertCliente(dto.getCliente());
        upsertMoneda(dto.getMoneda());
        upsertCircuitoContable(dto.getCircuitoContable());
        upsertDeposito(dto.getDeposito());
        upsertProvincia(dto.getProvincia());
        upsertPuntoVenta(dto.getPuntoDeVenta());
    }

    // ─── COMPROBANTES DE COMPRA ──────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncComprobanteCompra() {
        return syncComprobanteCompra(null, null);
    }

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncComprobanteCompra(String fechaDesde, String fechaHasta) {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "COMPROBANTE_COMPRA");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<ComprobanteCompraDTO> items =
                    xubioClient.getComprobanteCompra(tokenService.getBearerToken(), fechaDesde, fechaHasta);
            fetched = items.size();

            for (ComprobanteCompraDTO dto : items) {
                var existing = comprobanteCompraRepo.findByXubioId(dto.getComprobanteCompraId());
                if (existing.isPresent()) {
                    updateComprobanteCompra(existing.get(), dto);
                    updated++;
                } else {
                    createComprobanteCompra(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][COMPROBANTE_COMPRA] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][COMPROBANTE_COMPRA] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createComprobanteCompra(ComprobanteCompraDTO dto) {
        ComprobanteCompraEntity entity = new ComprobanteCompraEntity();
        mapComprobanteCompraToEntity(dto, entity);
        comprobanteCompraRepo.save(entity);
    }

    @Transactional
    protected void updateComprobanteCompra(ComprobanteCompraEntity entity, ComprobanteCompraDTO dto) {
        mapComprobanteCompraToEntity(dto, entity);
        comprobanteCompraRepo.save(entity);
    }

    private void mapComprobanteCompraToEntity(ComprobanteCompraDTO dto, ComprobanteCompraEntity entity) {
        entity.setXubioId(dto.getComprobanteCompraId());
        entity.setNumero(dto.getNumero());
        entity.setPuntoDeVenta(dto.getPuntoDeVenta() != null && dto.getPuntoDeVenta().getId() != null ? dto.getPuntoDeVenta().getId().intValue() : null);
        entity.setTipoDeComprobante(dto.getTipoDeComprobante());
        entity.setFecha(dto.getFecha());
        entity.setFechaVencimiento(dto.getFechaVencimiento());
        entity.setEstado(dto.getEstado());
        entity.setCondicionDePago(dto.getCondicionDePago());
        entity.setProveedorXubioId(dto.getProveedor() != null ? dto.getProveedor().getId() : null);
        entity.setMonedaId(dto.getMoneda() != null ? dto.getMoneda().getId() : null);
        entity.setCotizacion(dto.getCotizacion());
        entity.setSubtotal(dto.getSubtotal());
        entity.setDescuento(dto.getDescuento());
        entity.setTotalImpuestos(dto.getTotalImpuestos());
        entity.setTotal(dto.getTotal());
        entity.setSaldoPendiente(dto.getSaldoPendiente());
        entity.setObservaciones(dto.getObservaciones());
        entity.setImporteMonPrincipal(dto.getImporteMonPrincipal());
        entity.setCircuitoContableId(dto.getCircuitoContable() != null ? dto.getCircuitoContable().getId() : null);
        entity.setFechaComprobante(dto.getFechaComprobante());
        entity.setFechaFiscal(dto.getFechaFiscal());
        entity.setCbuInformada(dto.getCbuInformada());
        entity.setRawJson(toJson(dto));

        upsertProveedor(dto.getProveedor());
        upsertMoneda(dto.getMoneda());
        upsertCircuitoContable(dto.getCircuitoContable());
        upsertPuntoVenta(dto.getPuntoDeVenta());
    }

    // ─── COBRANZAS ───────────────────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncCobranzas() {
        return syncCobranzas(null, null);
    }

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncCobranzas(String fechaDesde, String fechaHasta) {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "COBRANZAS");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<CobranzaDTO> items =
                    xubioClient.getCobranzas(tokenService.getBearerToken(), fechaDesde, fechaHasta);
            fetched = items.size();

            for (CobranzaDTO dto : items) {
                var existing = cobranzaRepo.findByXubioId(dto.getCobranzaId());
                if (existing.isPresent()) {
                    updateCobranza(existing.get(), dto);
                    updated++;
                } else {
                    createCobranza(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][COBRANZAS] Sync completo: {} fetched, {} creadas, {} actualizadas en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][COBRANZAS] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createCobranza(CobranzaDTO dto) {
        CobranzaEntity entity = new CobranzaEntity();
        mapCobranzaToEntity(dto, entity);
        cobranzaRepo.save(entity);
    }

    @Transactional
    protected void updateCobranza(CobranzaEntity entity, CobranzaDTO dto) {
        mapCobranzaToEntity(dto, entity);
        cobranzaRepo.save(entity);
    }

    private void mapCobranzaToEntity(CobranzaDTO dto, CobranzaEntity entity) {
        entity.setXubioId(dto.getCobranzaId());
        entity.setNumero(dto.getNumero());
        entity.setFecha(dto.getFecha() != null ? dto.getFecha().toLocalDate() : null);
        entity.setClienteXubioId(dto.getCliente() != null ? dto.getCliente().getId() : null);
        entity.setComprobanteVentaId(dto.getComprobanteVenta() != null ? dto.getComprobanteVenta().getId() : null);
        entity.setMonedaId(dto.getMonedaCtaCte() != null ? dto.getMonedaCtaCte().getId() : null);
        entity.setCotizacion(dto.getCotizacion());
        entity.setCircuitoContableId(dto.getCircuitoContable() != null ? dto.getCircuitoContable().getId() : null);
        entity.setUtilizaMonedaExtranjera(dto.getUtilizaMonedaExtranjera());
        entity.setObservaciones(dto.getObservaciones());
        entity.setRawJson(toJson(dto));

        upsertCliente(dto.getCliente());
        upsertMoneda(dto.getMonedaCtaCte());
        upsertCircuitoContable(dto.getCircuitoContable());

        entity.getInstrumentos().clear();
        if (dto.getTransaccionInstrumentoDeCobro() != null) {
            for (TransaccionInstrumentoDeCobroDTO item : dto.getTransaccionInstrumentoDeCobro()) {
                CobranzaInstrumentoEntity instrumento = new CobranzaInstrumentoEntity();
                instrumento.setCobranza(entity);
                instrumento.setTransaccionIcId(item.getTransaccionICId());
                instrumento.setCuentaTipo(item.getCuentaTipo());
                instrumento.setCuentaId(item.getCuenta() != null ? item.getCuenta().getId() : null);
                instrumento.setMonedaId(item.getMoneda() != null ? item.getMoneda().getId() : null);
                instrumento.setCotizacion(item.getCotizacion());
                instrumento.setImporte(item.getImporte());
                instrumento.setDescripcion(item.getDescripcion());
                entity.getInstrumentos().add(instrumento);

                upsertCuentaBancaria(item.getCuenta());
                upsertMoneda(item.getMoneda());
            }
        }

        entity.getRetenciones().clear();
        if (dto.getTransaccionRetencionItems() != null) {
            for (TransaccionRetencionItemDTO item : dto.getTransaccionRetencionItems()) {
                CobranzaRetencionEntity retencion = new CobranzaRetencionEntity();
                retencion.setCobranza(entity);
                retencion.setTransaccionRItemId(item.getTransaccionRItemId());
                retencion.setTipoRetencion(item.getTipoRetencion());
                retencion.setConceptoRetencionId(item.getConceptoRetencion() != null ? item.getConceptoRetencion().getId() : null);
                retencion.setDescripcion(item.getDescripcion());
                retencion.setNumeroComprobante(item.getNumeroComprobante());
                retencion.setMonedaId(item.getMoneda() != null ? item.getMoneda().getId() : null);
                retencion.setCotizacion(item.getCotizacion());
                retencion.setImporteISAR(item.getImporteISAR());
                retencion.setImporteRetenido(item.getImporteRetenido());
                retencion.setImporteMonPpal(item.getImporteMonPpal());
                retencion.setFechaComprobante(item.getFechaComprobante() != null ? item.getFechaComprobante().toLocalDate() : null);
                entity.getRetenciones().add(retencion);

                upsertConceptoRetencion(item.getConceptoRetencion());
                upsertMoneda(item.getMoneda());
            }
        }
    }

    // ─── PAGOS ───────────────────────────────────────────────────────────────

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncPagos() {
        return syncPagos(null, null);
    }

    //@Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public SyncResult syncPagos(String fechaDesde, String fechaHasta) {
        long start = System.currentTimeMillis();
        SyncExecutionLogEntity logEntry = syncLogService.startLog("XUBIO", "PAGOS");
        int fetched = 0, created = 0, updated = 0;

        try {
            List<PagoDTO> items =
                    xubioClient.getPagos(tokenService.getBearerToken(), fechaDesde, fechaHasta);
            fetched = items.size();

            for (PagoDTO dto : items) {
                var existing = pagoRepo.findByXubioId(dto.getTransaccionId());
                if (existing.isPresent()) {
                    updatePago(existing.get(), dto);
                    updated++;
                } else {
                    createPago(dto);
                    created++;
                }
            }

            long duration = System.currentTimeMillis() - start;
            syncLogService.completeLog(logEntry, fetched, created, updated, duration);
            log.info("[XUBIO][PAGOS] Sync completo: {} fetched, {} creados, {} actualizados en {}ms",
                    fetched, created, updated, duration);
            return new SyncResult(fetched, created, updated);

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            syncLogService.failLog(logEntry, e.getMessage(), duration);
            log.error("[XUBIO][PAGOS] Error en sync: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    protected void createPago(PagoDTO dto) {
        PagoEntity entity = new PagoEntity();
        mapPagoToEntity(dto, entity);
        pagoRepo.save(entity);
    }

    @Transactional
    protected void updatePago(PagoEntity entity, PagoDTO dto) {
        mapPagoToEntity(dto, entity);
        pagoRepo.save(entity);
    }

    private void mapPagoToEntity(PagoDTO dto, PagoEntity entity) {
        entity.setXubioId(dto.getTransaccionId());
        entity.setCircuitoContableId(dto.getCircuitoContable() != null ? dto.getCircuitoContable().getId() : null);
        entity.setProveedorXubioId(dto.getProveedor() != null ? dto.getProveedor().getId() : null);
        entity.setFecha(dto.getFecha());
        entity.setNumeroRecibo(dto.getNumeroRecibo());
        entity.setCotizacion(dto.getCotizacion());
        entity.setUtilizaMonedaExtranjera(dto.getUtilizaMonedaExtranjera());
        entity.setObservacion(dto.getObservacion());
        entity.setMonedaId(dto.getMoneda() != null ? dto.getMoneda().getId() : null);
        entity.setRawJson(toJson(dto));

        upsertProveedor(dto.getProveedor());
        upsertMoneda(dto.getMoneda());
        upsertCircuitoContable(dto.getCircuitoContable());

        entity.getInstrumentos().clear();
        if (dto.getTransaccionInstrumentoDePago() != null) {
            for (TransaccionInstrumentoDePagoDTO item : dto.getTransaccionInstrumentoDePago()) {
                PagoInstrumentoEntity instrumento = new PagoInstrumentoEntity();
                instrumento.setPago(entity);
                instrumento.setTransaccionIpId(item.getTransaccionIPId());
                instrumento.setTipoCuenta(item.getTipoCuenta());
                instrumento.setCuentaId(item.getCuenta() != null ? item.getCuenta().getId() : null);
                instrumento.setMonedaId(item.getMoneda() != null ? item.getMoneda().getId() : null);
                instrumento.setCotizacion(item.getCotizacion());
                instrumento.setImporte(item.getImporte());
                instrumento.setChequeTerceros(item.getChequeTerceros());
                instrumento.setChequePropio(item.getChequePropio());
                instrumento.setVencimientoCheque(item.getVencimientoCheque());
                instrumento.setBancoId(item.getBanco() != null ? item.getBanco().getId() : null);
                instrumento.setDescripcion(item.getDescripcion());
                entity.getInstrumentos().add(instrumento);

                upsertCuentaBancaria(item.getCuenta());
                upsertMoneda(item.getMoneda());
                upsertBanco(item.getBanco());
            }
        }

        entity.getRetenciones().clear();
        if (dto.getTransaccionRetencionItems() != null) {
            for (TransaccionRetencionItemDTO item : dto.getTransaccionRetencionItems()) {
                PagoRetencionEntity retencion = new PagoRetencionEntity();
                retencion.setPago(entity);
                retencion.setTransaccionRItemId(item.getTransaccionRItemId());
                retencion.setTipoRetencion(item.getTipoRetencion());
                retencion.setConceptoRetencionId(item.getConceptoRetencion() != null ? item.getConceptoRetencion().getId() : null);
                retencion.setDescripcion(item.getDescripcion());
                retencion.setNumeroComprobante(item.getNumeroComprobante());
                retencion.setMonedaId(item.getMoneda() != null ? item.getMoneda().getId() : null);
                retencion.setCotizacion(item.getCotizacion());
                retencion.setImporteISAR(item.getImporteISAR());
                retencion.setImporteRetenido(item.getImporteRetenido());
                retencion.setImporteMonPpal(item.getImporteMonPpal());
                retencion.setFechaComprobante(item.getFechaComprobante() != null ? item.getFechaComprobante().toLocalDate() : null);
                entity.getRetenciones().add(retencion);

                upsertConceptoRetencion(item.getConceptoRetencion());
                upsertMoneda(item.getMoneda());
            }
        }
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    /**
     * Upserts de las referencias livianas (moneda, circuitoContable, deposito,
     * puntoVenta, provincia, banco, cuentaBancaria) que Xubio embebe dentro de
     * otras entidades y para las que no existe un endpoint de sincronización
     * propio: se van completando a medida que aparecen referenciadas.
     */
    @Transactional
    protected void upsertMoneda(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        MonedaEntity entity = monedaRepo.findByXubioId(ref.getId()).orElseGet(MonedaEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        monedaRepo.save(entity);
    }

    @Transactional
    protected void upsertCircuitoContable(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        CircuitoContableEntity entity = circuitoContableRepo.findByXubioId(ref.getId()).orElseGet(CircuitoContableEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        circuitoContableRepo.save(entity);
    }

    @Transactional
    protected void upsertDeposito(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        DepositoEntity entity = depositoRepo.findByXubioId(ref.getId()).orElseGet(DepositoEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        depositoRepo.save(entity);
    }

    @Transactional
    protected void upsertPuntoVenta(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        PuntoVentaEntity entity = puntoVentaRepo.findByXubioId(ref.getId()).orElseGet(PuntoVentaEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        puntoVentaRepo.save(entity);
    }

    @Transactional
    protected void upsertProvincia(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        ProvinciaEntity entity = provinciaRepo.findByXubioId(ref.getId()).orElseGet(ProvinciaEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        provinciaRepo.save(entity);
    }

    @Transactional
    protected void upsertBanco(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        BancoEntity entity = bancoRepo.findByXubioId(ref.getId()).orElseGet(BancoEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        bancoRepo.save(entity);
    }

    @Transactional
    protected void upsertCuentaBancaria(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        CuentaBancariaEntity entity = cuentaBancariaRepo.findByXubioId(ref.getId()).orElseGet(CuentaBancariaEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        cuentaBancariaRepo.save(entity);
    }

    @Transactional
    protected void upsertConceptoRetencion(EntidadRefDTO ref) {
        if (ref == null || ref.getId() == null) {
            return;
        }
        ConceptoRetencionEntity entity = conceptoRetencionRepo.findByXubioId(ref.getId()).orElseGet(ConceptoRetencionEntity::new);
        entity.setXubioId(ref.getId());
        entity.setNombre(ref.getNombre());
        entity.setCodigo(ref.getCodigo());
        conceptoRetencionRepo.save(entity);
    }

    /** Asegura que el cliente referenciado exista en xubio_clientes, aunque el sync de clientes no lo haya traído todavía. */
    @Transactional
    protected void upsertCliente(EntidadRefDTO cliente) {
        if (cliente == null || cliente.getId() == null) {
            return;
        }
        String xubioId = String.valueOf(cliente.getId());
        XubioClienteEntity entity = clienteRepo.findByXubioId(xubioId).orElseGet(XubioClienteEntity::new);
        entity.setXubioId(xubioId);
        entity.setClienteId(xubioId);
        entity.setNombre(cliente.getNombre());
        clienteRepo.save(entity);
    }

    /** Asegura que el proveedor referenciado exista en xubio_proveedores, aunque el sync de proveedores no lo haya traído todavía. */
    @Transactional
    protected void upsertProveedor(EntidadRefDTO proveedor) {
        if (proveedor == null || proveedor.getId() == null) {
            return;
        }
        String xubioId = String.valueOf(proveedor.getId());
        XubioProveedorEntity entity = proveedorRepo.findByXubioId(xubioId).orElseGet(XubioProveedorEntity::new);
        entity.setXubioId(xubioId);
        entity.setProveedorId(xubioId);
        entity.setNombre(proveedor.getNombre());
        proveedorRepo.save(entity);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public record SyncResult(int fetched, int created, int updated) {}
}
