package com.sales_microservice.integration_tests;


import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.dto.SalesDetailDTO;
import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.entities.SalesDetail;
import com.sales_microservice.sales.mapper.SalesMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SalesMapperTest {

    private SalesMapper salesMapper = new SalesMapper();

    private Sales salesEntity;
    private SalesDetail salesDetailEntity;

    @BeforeEach
    void setUp() {
        salesEntity = new Sales();
        salesEntity.setId(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"));
        salesEntity.setClienteNombre("Ana López");
        salesEntity.setClienteEmail("ana@example.com");
        salesEntity.setFecha(LocalDateTime.now());
        salesEntity.setTotal(new BigDecimal("150.75"));
        salesEntity.setMetodoPago("EFECTIVO");
        salesEntity.setEstado("PENDIENTE");

        salesDetailEntity = new SalesDetail();
        salesDetailEntity.setId(1L);
        salesDetailEntity.setProductoId(101L);
        salesDetailEntity.setProductoNombre("Laptop Gamer");
        salesDetailEntity.setCantidad(1);
        salesDetailEntity.setPrecioUnitario(new BigDecimal("150.75"));
        salesDetailEntity.setSubtotal(new BigDecimal("150.75"));
        salesDetailEntity.setSales(salesEntity);

        salesEntity.setDetalles(Arrays.asList(salesDetailEntity));
    }

    @Test
    void convertirEntidadADTO_DeberiaMapearCorrectamente() {
        SalesDTO dto = salesMapper.toDto(salesEntity);

        assertNotNull(dto);
        assertEquals(salesEntity.getId(), dto.getId());
        assertEquals(salesEntity.getClienteNombre(), dto.getClienteNombre());
        assertEquals(salesEntity.getClienteEmail(), dto.getClienteEmail());
        assertEquals(salesEntity.getTotal(), dto.getTotal());
        assertFalse(dto.getDetalles().isEmpty());
    }

    @Test
    void convertirDetalleEntidadADTO_DeberiaMapearCorrectamente() {
        SalesDetailDTO detailDTO = salesMapper.toDetailDto(salesDetailEntity);

        assertNotNull(detailDTO);
        assertEquals(salesDetailEntity.getId(), detailDTO.getId());
        assertEquals(salesDetailEntity.getProductoId(), detailDTO.getProductoId());
        assertEquals(salesDetailEntity.getProductoNombre(), detailDTO.getProductoNombre());
        assertEquals(salesDetailEntity.getSubtotal(), detailDTO.getSubtotal());
    }

    @Test
    void convertirEntidadSinDetallesADTO_DeberiaManejarListaVacia() {
        salesEntity.setDetalles(null);
        SalesDTO dto = salesMapper.toDto(salesEntity);

        assertNotNull(dto);
        assertNull(dto.getDetalles());
    }
}
