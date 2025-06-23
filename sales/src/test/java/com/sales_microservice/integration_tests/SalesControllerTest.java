package com.sales_microservice.integration_tests;



import com.sales_microservice.sales.controller.SalesController;
import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.exception.SalesNotFoundException;
import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.service.ISalesService; // Cambiado a la interfaz
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesControllerTest {

    @Mock
    private ISalesService salesService; // Usando la interfaz ISalesService

    @Mock
    private AuthService authService;

    @InjectMocks
    private SalesController salesController;

    private SalesDTO salesDTO;
    private final String validToken = "Bearer validToken";
    private final String invalidToken = "Bearer invalidToken";

    @BeforeEach
    void setUp() {
        salesDTO = new SalesDTO();
        salesDTO.setId(1L);
        salesDTO.setClienteNombre("Juan Pérez");
        salesDTO.setClienteEmail("juan@example.com");
        salesDTO.setFecha(LocalDateTime.now());
        salesDTO.setTotal(new BigDecimal("100.50"));
        salesDTO.setMetodoPago("TARJETA");
        salesDTO.setEstado("PENDIENTE");
    }

    @Test
    void cuandoCrearVentaConTokenValido_deberiaRetornarVentaCreada() {
        when(authService.isValidToken(validToken)).thenReturn(true);
        when(salesService.createSales(any(SalesDTO.class))).thenReturn(salesDTO);

        ResponseEntity<SalesDTO> response = salesController.createSales(salesDTO, validToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(salesDTO.getId(), response.getBody().getId());
        verify(salesService, times(1)).createSales(any(SalesDTO.class));
    }

    @Test
    void cuandoObtenerTodasLasVentasConTokenValido_deberiaRetornarLista() {
        when(authService.isValidToken(validToken)).thenReturn(true);
        List<SalesDTO> ventas = Arrays.asList(salesDTO);
        when(salesService.getAllSales()).thenReturn(ventas);

        ResponseEntity<List<SalesDTO>> response = salesController.getAllSales(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void cuandoObtenerVentaPorIdExistente_deberiaRetornarVenta() {
        when(authService.isValidToken(validToken)).thenReturn(true);
        when(salesService.getSalesById(1L)).thenReturn(salesDTO);

        ResponseEntity<SalesDTO> response = salesController.getSalesById(1L, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(salesDTO.getId(), response.getBody().getId());
    }

    @Test
    void cuandoObtenerVentaPorIdNoExistente_deberiaLanzarExcepcion() {
        when(authService.isValidToken(validToken)).thenReturn(true);
        when(salesService.getSalesById(999L)).thenThrow(new SalesNotFoundException("Venta no encontrada"));

        assertThrows(SalesNotFoundException.class, () -> {
            salesController.getSalesById(999L, validToken);
        });
    }
}