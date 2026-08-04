package com.sync.client;

import com.sync.dto.xubio.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para la API de Xubio.
 * Documentación: https://xubio.com/API/1.1/docs
 *
 * Todos los endpoints requieren el header Authorization: Bearer {token}
 * El token se obtiene via OAuth2 client_credentials.
 */
@FeignClient(name = "xubio-client", url = "${xubio.api.base-url}", configuration = com.sync.config.FeignConfig.class)
public interface XubioClient {

    // ─── AUTH ───────────────────────────────────────────────────────────────

    @PostMapping(value = "/TokenEndpoint", consumes = "application/x-www-form-urlencoded")
    TokenResponse getToken(
            @RequestHeader("Authorization") String basicAuth,
            Map<String, ?> body
    );

    // ─── CLIENTES ────────────────────────────────────────────────────────────

    @GetMapping("/clienteBean")
    List<ClienteDTO> getClientes(
                    @RequestHeader("Authorization") String bearerToken);

    // ─── PROVEEDOR ────────────────────────────────────────────────────────────

    @GetMapping("/ProveedorBean")
    List<ProveedorDTO> getProveedores(
                    @RequestHeader("Authorization") String bearerToken);

    // ─── COMPROBANTE VENTA ──────────────────────────────────────────────────────

    @GetMapping("/comprobanteVentaBean")
    List<ComprobanteVentaDTO> getComprobanteVenta(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false) String fechaHasta);

    // ─── COMPROBANTE COMPRA ──────────────────────────────────────────────────────

    @GetMapping("/comprobanteCompraBean")
    List<ComprobanteCompraDTO> getComprobanteCompra(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false) String fechaHasta);

    // ─── COBRANZA ────────────────────────────────────────────────────────────────

    @GetMapping("/cobranzaBean")
    List<CobranzaDTO> getCobranzas(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false) String fechaHasta);

    // ─── PAGO ────────────────────────────────────────────────────────────────────

    @GetMapping("/pagoBean")
    List<PagoDTO> getPagos(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam(value = "fechaDesde", required = false) String fechaDesde,
            @RequestParam(value = "fechaHasta", required = false) String fechaHasta);
}
