package com.sales_microservice.unit_tests;


import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.dto.SalesDetailDTO;
import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.entities.SalesDetail;
import com.sales_microservice.sales.mapper.SalesMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SalesMapperTest {

    private SalesMapper mapper = new SalesMapper();

    @Test
    void toDto_DeberiaMapearCorrectamenteVenta() {
        // Configurar entidad Sales
        Sales venta = new Sales();
        venta.setId(1L);
        venta.setClienteNombre("Ana López");
        venta.setClienteEmail("ana@example.com");
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(new BigDecimal("150.75"));
        venta.setMetodoPago("EFECTIVO");
        venta.setEstado("PENDIENTE");

        // Configurar detalles
        SalesDetail detalle = new SalesDetail();
        detalle.setId(1L);
        detalle.setProductoId(101L);
        detalle.setProductoNombre("Laptop");
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(new BigDecimal("150.75"));
        detalle.setSubtotal(new BigDecimal("150.75"));
        venta.setDetalles(Arrays.asList(detalle));

        // Mapear a DTO
        SalesDTO dto = mapper.toDto(venta);

        // Verificaciones
        assertNotNull(dto);
        assertEquals(venta.getId(), dto.getId());
        assertEquals(venta.getClienteNombre(), dto.getClienteNombre());
        assertEquals(venta.getTotal(), dto.getTotal());
        assertFalse(dto.getDetalles().isEmpty());
        assertEquals(1, dto.getDetalles().size());

        SalesDetailDTO detalleDTO = dto.getDetalles().get(0);
        assertEquals(detalle.getProductoNombre(), detalleDTO.getProductoNombre());
        assertEquals(detalle.getSubtotal(), detalleDTO.getSubtotal());
    }

    @Test
    void toDetailDto_DeberiaMapearCorrectamenteDetalle() {
        SalesDetail detalle = new SalesDetail();
        detalle.setId(1L);
        detalle.setProductoId(101L);
        detalle.setProductoNombre("Teclado");
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(new BigDecimal("25.50"));
        detalle.setSubtotal(new BigDecimal("51.00"));

        SalesDetailDTO dto = mapper.toDetailDto(detalle);

        assertNotNull(dto);
        assertEquals(detalle.getId(), dto.getId());
        assertEquals(detalle.getProductoNombre(), dto.getProductoNombre());
        assertEquals(detalle.getCantidad(), dto.getCantidad());
        assertEquals(detalle.getSubtotal(), dto.getSubtotal());
    }
}