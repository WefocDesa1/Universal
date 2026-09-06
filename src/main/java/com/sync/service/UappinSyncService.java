package com.sync.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.sync.client.UappinSftpClient;
import com.sync.dto.uappin.UappinServicioDTO;
import com.sync.entity.uappin.UappinArchivoProcesadoEntity;
import com.sync.entity.uappin.UappinServicioEntity;
import com.sync.repository.uappin.UappinArchivoProcesadoRepository;
import com.sync.repository.uappin.UappinServicioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Sincroniza los reportes de servicios de Uappin: los levanta por SFTP,
 * los parsea y hace upsert en uappin_servicios. Cada archivo procesado
 * (ok o con error) queda registrado en uappin_archivos_procesados, y el
 * archivo se mueve en el propio SFTP a "procesados" o "error" según
 * corresponda, para no volver a levantarlo en la próxima corrida.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UappinSyncService {

    private final UappinSftpClient sftpClient;
    private final UappinExcelParser excelParser;
    private final UappinServicioRepository servicioRepo;
    private final UappinArchivoProcesadoRepository archivoRepo;

    public SyncResult syncServicios() {
        int archivosProcesados = 0;
        int archivosConError = 0;
        int filasTotales = 0;
        ChannelSftp channel = null;

        try {
            channel = sftpClient.connect();
            List<String> archivos = sftpClient.listXlsxFiles(channel);
            log.info("[UAPPIN] {} archivo(s) .xlsx encontrados en el inbox del SFTP", archivos.size());

            for (String archivo : archivos) {
                if (archivoRepo.existsByNombreArchivo(archivo)) {
                    log.info("[UAPPIN] Archivo {} ya estaba procesado, se omite", archivo);
                    continue;
                }

                try {
                    byte[] bytes = sftpClient.download(channel, archivo);
                    List<UappinServicioDTO> filas = excelParser.parse(bytes);
                    int[] creadasActualizadas = upsertFilas(filas);

                    sftpClient.moveToProcesados(channel, archivo);
                    registrarResultado(archivo, "PROCESADO", filas.size(),
                            creadasActualizadas[0], creadasActualizadas[1], null);

                    archivosProcesados++;
                    filasTotales += filas.size();
                    log.info("[UAPPIN] Archivo {} procesado: {} filas ({} creadas, {} actualizadas)",
                            archivo, filas.size(), creadasActualizadas[0], creadasActualizadas[1]);

                } catch (Exception e) {
                    log.error("[UAPPIN] Error procesando archivo {}: {}", archivo, e.getMessage(), e);
                    try {
                        sftpClient.moveToError(channel, archivo);
                    } catch (SftpException moveEx) {
                        log.error("[UAPPIN] Además falló mover {} a la carpeta de error: {}", archivo, moveEx.getMessage());
                    }
                    registrarResultado(archivo, "ERROR", null, null, null, e.getMessage());
                    archivosConError++;
                }
            }

        } catch (Exception e) {
            log.error("[UAPPIN] Error conectando o listando el SFTP: {}", e.getMessage(), e);
            throw new RuntimeException("Error en sync de Uappin: " + e.getMessage(), e);
        } finally {
            sftpClient.disconnect(channel);
        }

        return new SyncResult(archivosProcesados, archivosConError, filasTotales);
    }

    @Transactional
    protected int[] upsertFilas(List<UappinServicioDTO> filas) {
        int creadas = 0;
        int actualizadas = 0;
        for (UappinServicioDTO dto : filas) {
            if (dto.getNroServicio() == null) {
                continue;
            }
            var existing = servicioRepo.findByNroServicio(dto.getNroServicio());
            UappinServicioEntity entity = existing.orElseGet(UappinServicioEntity::new);
            mapDtoToEntity(dto, entity);
            servicioRepo.save(entity);
            if (existing.isPresent()) {
                actualizadas++;
            } else {
                creadas++;
            }
        }
        return new int[]{creadas, actualizadas};
    }

    private void mapDtoToEntity(UappinServicioDTO dto, UappinServicioEntity entity) {
        entity.setNroServicio(dto.getNroServicio());
        entity.setFechaInicioToma(dto.getFechaInicioToma());
        entity.setTomadorReserva(dto.getTomadorReserva());
        entity.setFechaFinToma(dto.getFechaFinToma());
        entity.setFormaIngreso(dto.getFormaIngreso());
        entity.setCliente(dto.getCliente());
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setDocumentoNumero(dto.getDocumentoNumero());
        entity.setPasajeroNombre(dto.getPasajeroNombre());
        entity.setOrigen(dto.getOrigen());
        entity.setDestino(dto.getDestino());
        entity.setPrecioServicio(dto.getPrecioServicio());
        entity.setPrecioAdicionales(dto.getPrecioAdicionales());
        entity.setPlusNocturno(dto.getPlusNocturno());
        entity.setTotalServicio(dto.getTotalServicio());
        entity.setComisionChoferPorServicio(dto.getComisionChoferPorServicio());
        entity.setGastosChofer(dto.getGastosChofer());
        entity.setFechaServicio(dto.getFechaServicio());
        entity.setHoraServicio(dto.getHoraServicio());
        entity.setFechaFinServicio(dto.getFechaFinServicio());
        entity.setHoraFinServicio(dto.getHoraFinServicio());
        entity.setDuracionServicio(dto.getDuracionServicio());
        entity.setKmsRealizados(dto.getKmsRealizados());
        entity.setCategoriaServicio(dto.getCategoriaServicio());
        entity.setAdicionalesServicio(dto.getAdicionalesServicio());
        entity.setAtributosServicio(dto.getAtributosServicio());
        entity.setApu(dto.getApu());
        entity.setCodigoConductor(dto.getCodigoConductor());
        entity.setConductor(dto.getConductor());
        entity.setConductorPropio(dto.getConductorPropio());
        entity.setMovil(dto.getMovil());
        entity.setPatente(dto.getPatente());
        entity.setTipoDePago(dto.getTipoDePago());
        entity.setUnidadNegocio(dto.getUnidadNegocio());
        entity.setEstadoServicio(dto.getEstadoServicio());
        entity.setLiquidacionChofer(dto.getLiquidacionChofer());
        entity.setOrdenNro(dto.getOrdenNro());
        entity.setFacturaNro(dto.getFacturaNro());
    }

    @Transactional
    protected void registrarResultado(String nombreArchivo, String estado, Integer filasLeidas,
                                       Integer filasCreadas, Integer filasActualizadas, String errorMensaje) {
        UappinArchivoProcesadoEntity entity = new UappinArchivoProcesadoEntity();
        entity.setNombreArchivo(nombreArchivo);
        entity.setEstado(estado);
        entity.setFilasLeidas(filasLeidas);
        entity.setFilasCreadas(filasCreadas);
        entity.setFilasActualizadas(filasActualizadas);
        entity.setErrorMensaje(errorMensaje);
        archivoRepo.save(entity);
    }

    public record SyncResult(int archivosProcesados, int archivosConError, int filasTotales) {}
}
