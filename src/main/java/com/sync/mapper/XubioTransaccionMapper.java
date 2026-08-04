package com.sync.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sync.dto.xubio.TransaccionOrdenPagoItemDTO;
import com.sync.dto.xubio.TransaccionPercepcionItemDTO;
import com.sync.dto.xubio.TransaccionProductoItemDTO;
import com.sync.dto.xubio.TransaccionDTO;
import com.sync.entity.xubio.XubioTransaccionEntity;
import com.sync.entity.xubio.XubioTransaccionOrdenPagoItemEntity;
import com.sync.entity.xubio.XubioTransaccionPercepcionItemEntity;
import com.sync.entity.xubio.XubioTransaccionProductoItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Mapper para convertir DTOs de Xubio Transacciones a entidades JPA.
 */
@Component
@RequiredArgsConstructor
public class XubioTransaccionMapper {

    private final ObjectMapper objectMapper;

    public XubioTransaccionEntity dtoToEntity(TransaccionDTO dto) {
        XubioTransaccionEntity entity = new XubioTransaccionEntity();

        entity.setTransaccionId(dto.getTransaccionId());
        entity.setExternalId(dto.getExternalId());
        entity.setTipo(dto.getTipo());
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        entity.setFechaFiscal(dto.getFechaFiscal());
        entity.setFechaComprobante(dto.getFechaComprobante());
        entity.setFechaVto(dto.getFechaVto());
        entity.setNumeroDocumento(dto.getNumeroDocumento());
        entity.setPuntoVenta(dto.getPuntoVenta());
        entity.setComprobante(dto.getComprobante());
        entity.setCondicionDePago(dto.getCondicionDePago());

        // Extraer IDs de referencias
        if (dto.getCircuitoContable() != null) {
            entity.setCircuitoContableId(dto.getCircuitoContable().getId());
        }
        if (dto.getCuentaBancaria() != null) {
            entity.setCuentaBancariaId(dto.getCuentaBancaria().getId());
        }
        if (dto.getDeposito() != null) {
            entity.setDepositoId(dto.getDeposito().getId());
        }
        if (dto.getListaDePrecio() != null) {
            entity.setListaDePrecioId(dto.getListaDePrecio().getId());
        }
        if (dto.getProveedor() != null) {
            entity.setProveedorId(dto.getProveedor().getId());
        }
        if (dto.getProvincia() != null) {
            entity.setProvinciaId(dto.getProvincia().getProvinciaId());
        }

        entity.setCotizacion(dto.getCotizacion());
        entity.setImporteMonPrincipal(dto.getImporteMonPrincipal());
        entity.setImporteTotal(dto.getImporteTotal());
        entity.setImporteImpuestos(dto.getImporteImpuestos());
        entity.setImporteGravado(dto.getImporteGravado());
        entity.setCotizacionListaDePrecio(dto.getCotizacionListaDePrecio());
        entity.setCbuinformada(dto.getCbuinformada());
        entity.setRawJson(toJson(dto));

        // Mapear items
        if (dto.getTransaccionProductoItems() != null) {
            entity.setProductoItems(
                    dto.getTransaccionProductoItems().stream()
                            .map(itemDto -> productoItemDtoToEntity(itemDto, entity))
                            .collect(Collectors.toList())
            );
        } else {
            entity.setProductoItems(Collections.emptyList());
        }

        if (dto.getTransaccionPercepcionItems() != null) {
            entity.setPercepcionItems(
                    dto.getTransaccionPercepcionItems().stream()
                            .map(itemDto -> percepcionItemDtoToEntity(itemDto, entity))
                            .collect(Collectors.toList())
            );
        } else {
            entity.setPercepcionItems(Collections.emptyList());
        }

        if (dto.getTransaccionOrdenPagoItems() != null) {
            entity.setOrdenPagoItems(
                    dto.getTransaccionOrdenPagoItems().stream()
                            .map(itemDto -> ordenPagoItemDtoToEntity(itemDto, entity))
                            .collect(Collectors.toList())
            );
        } else {
            entity.setOrdenPagoItems(Collections.emptyList());
        }

        return entity;
    }

    private XubioTransaccionProductoItemEntity productoItemDtoToEntity(
            TransaccionProductoItemDTO dto,
            XubioTransaccionEntity transaccion) {
        XubioTransaccionProductoItemEntity entity = new XubioTransaccionProductoItemEntity();
        entity.setTransaccion(transaccion);
        entity.setTransaccionCVItemId(dto.getTransaccionCVItemId());
        entity.setProductoId(dto.getProducto() != null ? dto.getProducto().getId() : null);
        entity.setCentroDeCostoId(dto.getCentroDeCosto() != null ? dto.getCentroDeCosto().getId() : null);
        entity.setDepositoId(dto.getDeposito() != null ? dto.getDeposito().getId() : null);
        entity.setDescripcion(dto.getDescripcion());
        entity.setCantidad(dto.getCantidad());
        entity.setPrecio(dto.getPrecio());
        entity.setPrecioConIvaIncluido(dto.getPrecioConIvaIncluido());
        entity.setIva(dto.getIva());
        entity.setImporte(dto.getImporte());
        entity.setTotal(dto.getTotal());
        entity.setMontoExento(dto.getMontoExento());
        entity.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        entity.setRawJson(toJson(dto));
        return entity;
    }

    private XubioTransaccionPercepcionItemEntity percepcionItemDtoToEntity(
            TransaccionPercepcionItemDTO dto,
            XubioTransaccionEntity transaccion) {
        XubioTransaccionPercepcionItemEntity entity = new XubioTransaccionPercepcionItemEntity();
        entity.setTransaccion(transaccion);
        entity.setTransaccionCVItemId(dto.getTransaccionCVItemId());
        entity.setPercepcionImpuestoId(dto.getPercepcionImpuesto() != null ? dto.getPercepcionImpuesto().getId() : null);
        entity.setCentroDeCostoId(dto.getCentroDeCosto() != null ? dto.getCentroDeCosto().getId() : null);
        entity.setDescripcion(dto.getDescripcion());
        entity.setImporte(dto.getImporte());
        entity.setRawJson(toJson(dto));
        return entity;
    }

    private XubioTransaccionOrdenPagoItemEntity ordenPagoItemDtoToEntity(
            TransaccionOrdenPagoItemDTO dto,
            XubioTransaccionEntity transaccion) {
        XubioTransaccionOrdenPagoItemEntity entity = new XubioTransaccionOrdenPagoItemEntity();
        entity.setTransaccion(transaccion);
        entity.setItemId(dto.getItemId());
        entity.setCuentaTipo(dto.getCuentaTipo());
        entity.setCuentaId(dto.getCuentaId());
        entity.setMonedaId(dto.getMoneda() != null ? dto.getMoneda().getId() : null);
        entity.setCotizacionMonTransaccion(dto.getCotizacionMonTransaccion());
        entity.setImporteMonPrincipal(dto.getImporteMonPrincipal());
        entity.setImporteMonTransaccion(dto.getImporteMonTransaccion());
        entity.setChequeTerceros(dto.getChequeTerceros());
        entity.setChequePropios(dto.getChequePropios());
        entity.setFechaVto(dto.getFechaVto());
        entity.setBancoId(dto.getBanco() != null ? dto.getBanco().getId() : null);
        entity.setDescripcion(dto.getDescripcion());
        entity.setRawJson(toJson(dto));
        return entity;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
