package com.sync.service;

import com.sync.dto.uappin.UappinServicioDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Parsea el reporte de servicios de Uappin (excel con headers en la fila 1,
 * ej. columnas "NroServicio", "FechaInicioToma", etc.). Matchea columnas por
 * nombre de header en vez de posición fija, para no romperse si Uappin
 * reordena columnas entre entregas.
 */
@Slf4j
@Component
public class UappinExcelParser {

    private static final DateTimeFormatter FECHA_HORA_AMPM =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a", Locale.US);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    public List<UappinServicioDTO> parse(byte[] xlsxBytes) throws IOException {
        List<UappinServicioDTO> result = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(xlsxBytes))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            Map<String, Integer> columnIndex = buildColumnIndex(headerRow, dataFormatter);

            for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                String nroServicioRaw = getString(row, columnIndex, "NroServicio", dataFormatter);
                if (nroServicioRaw == null || nroServicioRaw.isBlank()) {
                    continue; // fila vacía
                }

                UappinServicioDTO dto = new UappinServicioDTO();
                dto.setNroServicio(getLong(row, columnIndex, "NroServicio", dataFormatter));
                dto.setFechaInicioToma(getFechaHoraAmPm(row, columnIndex, "FechaInicioToma", dataFormatter));
                dto.setTomadorReserva(getString(row, columnIndex, "TomadorReserva", dataFormatter));
                dto.setFechaFinToma(getFechaHoraAmPm(row, columnIndex, "FechaFinToma", dataFormatter));
                dto.setFormaIngreso(getString(row, columnIndex, "FormaIngreso", dataFormatter));
                dto.setCliente(getString(row, columnIndex, "Cliente", dataFormatter));
                dto.setTipoDocumento(getString(row, columnIndex, "TipoDocumento", dataFormatter));
                dto.setDocumentoNumero(getString(row, columnIndex, "DocumentoNumero", dataFormatter));
                dto.setPasajeroNombre(getString(row, columnIndex, "PasajeroNombre", dataFormatter));
                dto.setOrigen(getString(row, columnIndex, "Origen", dataFormatter));
                dto.setDestino(getString(row, columnIndex, "Destino", dataFormatter));
                dto.setPrecioServicio(getBigDecimal(row, columnIndex, "PrecioServicio", dataFormatter));
                dto.setPrecioAdicionales(getBigDecimal(row, columnIndex, "PrecioAdicionales", dataFormatter));
                dto.setPlusNocturno(getBigDecimal(row, columnIndex, "PlusNocturno", dataFormatter));
                dto.setTotalServicio(getBigDecimal(row, columnIndex, "TotalServicio", dataFormatter));
                dto.setComisionChoferPorServicio(getBigDecimal(row, columnIndex, "ComisionChoferPorServicio", dataFormatter));
                dto.setGastosChofer(getBigDecimal(row, columnIndex, "GastosChofer", dataFormatter));
                dto.setFechaServicio(getFecha(row, columnIndex, "FechaServicio", dataFormatter));
                dto.setHoraServicio(getHora(row, columnIndex, "HoraServicio", dataFormatter));
                dto.setFechaFinServicio(getFecha(row, columnIndex, "FechaFinServicio", dataFormatter));
                dto.setHoraFinServicio(getHora(row, columnIndex, "HoraFinServicio", dataFormatter));
                dto.setDuracionServicio(getInteger(row, columnIndex, "DuracionServicio", dataFormatter));
                dto.setKmsRealizados(getInteger(row, columnIndex, "KmsRealizados", dataFormatter));
                dto.setCategoriaServicio(getString(row, columnIndex, "CategoriaServicio", dataFormatter));
                dto.setAdicionalesServicio(getString(row, columnIndex, "AdicionalesServicio", dataFormatter));
                dto.setAtributosServicio(getString(row, columnIndex, "AtributosServicio", dataFormatter));
                dto.setApu(getString(row, columnIndex, "APU", dataFormatter));
                dto.setCodigoConductor(getString(row, columnIndex, "CodigoConductor", dataFormatter));
                dto.setConductor(getString(row, columnIndex, "Conductor", dataFormatter));
                dto.setConductorPropio(getString(row, columnIndex, "ConductorPropio", dataFormatter));
                dto.setMovil(getString(row, columnIndex, "Movil", dataFormatter));
                dto.setPatente(getString(row, columnIndex, "Patente", dataFormatter));
                dto.setTipoDePago(getString(row, columnIndex, "TipoDePago", dataFormatter));
                dto.setUnidadNegocio(getString(row, columnIndex, "UnidadNegocio", dataFormatter));
                dto.setEstadoServicio(getString(row, columnIndex, "EstadoServicio", dataFormatter));
                dto.setLiquidacionChofer(getString(row, columnIndex, "LiquidacionChofer", dataFormatter));
                dto.setOrdenNro(getLong(row, columnIndex, "OrdenNro", dataFormatter));
                dto.setFacturaNro(getString(row, columnIndex, "FacturaNro", dataFormatter));

                result.add(dto);
            }
        }

        log.info("Excel parseado: {} filas de datos", result.size());
        return result;
    }

    private Map<String, Integer> buildColumnIndex(Row headerRow, DataFormatter dataFormatter) {
        Map<String, Integer> index = new HashMap<>();
        for (Cell cell : headerRow) {
            String header = dataFormatter.formatCellValue(cell).trim();
            if (!header.isEmpty()) {
                index.put(header, cell.getColumnIndex());
            }
        }
        return index;
    }

    private Cell getCell(Row row, Map<String, Integer> columnIndex, String columnName) {
        Integer idx = columnIndex.get(columnName);
        if (idx == null) {
            return null;
        }
        return row.getCell(idx);
    }

    private String getString(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        Cell cell = getCell(row, columnIndex, columnName);
        if (cell == null) {
            return null;
        }
        String value = dataFormatter.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }

    private BigDecimal getBigDecimal(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        Cell cell = getCell(row, columnIndex, columnName);
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        String value = dataFormatter.formatCellValue(cell).trim();
        if (value.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.replace(",", "."));
        } catch (NumberFormatException e) {
            log.warn("No se pudo parsear '{}' como decimal en columna {}", value, columnName);
            return null;
        }
    }

    private Integer getInteger(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        BigDecimal value = getBigDecimal(row, columnIndex, columnName, dataFormatter);
        return value == null ? null : value.intValue();
    }

    private Long getLong(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        BigDecimal value = getBigDecimal(row, columnIndex, columnName, dataFormatter);
        return value == null ? null : value.longValue();
    }

    /** Formato fuente: "31/07/2026 02:22:39 p.m." */
    private LocalDateTime getFechaHoraAmPm(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        String value = getString(row, columnIndex, columnName, dataFormatter);
        if (value == null) {
            return null;
        }
        String cleaned = value
                .replace("a.m.", "AM").replace("p.m.", "PM")
                .replace("A.M.", "AM").replace("P.M.", "PM");
        try {
            return LocalDateTime.parse(cleaned, FECHA_HORA_AMPM);
        } catch (Exception e) {
            log.warn("No se pudo parsear '{}' como fecha/hora en columna {}", value, columnName);
            return null;
        }
    }

    /** Formato fuente: "31-07-2026" */
    private LocalDate getFecha(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        String value = getString(row, columnIndex, columnName, dataFormatter);
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value, FECHA);
        } catch (Exception e) {
            log.warn("No se pudo parsear '{}' como fecha en columna {}", value, columnName);
            return null;
        }
    }

    /** Formato fuente: "23:59" */
    private LocalTime getHora(Row row, Map<String, Integer> columnIndex, String columnName, DataFormatter dataFormatter) {
        String value = getString(row, columnIndex, columnName, dataFormatter);
        if (value == null) {
            return null;
        }
        try {
            return LocalTime.parse(value, HORA);
        } catch (Exception e) {
            log.warn("No se pudo parsear '{}' como hora en columna {}", value, columnName);
            return null;
        }
    }
}
