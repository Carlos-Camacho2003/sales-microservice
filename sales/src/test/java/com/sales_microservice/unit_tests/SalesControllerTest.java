package com.sales_microservice.unit_tests;

import com.sales_microservice.sales.controller.SalesController;
import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.service.ISalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

class SalesControllerTest {

    @Mock
    private ISalesService salesService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private SalesController salesController;

    private SalesDTO salesDTO;
    private String validToken = "Bearer validToken";
    private String invalidToken = "Bearer invalidToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar un token válido por defecto
        when(authService.isValidToken(validToken)).thenReturn(true);
        when(authService.isValidToken(invalidToken)).thenReturn(false);

        // Configurar un DTO de ejemplo
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
    void crearVenta_ConTokenValido_DeberiaRetornarVentaCreada() {
        when(salesService.createSales(any(SalesDTO.class))).thenReturn(salesDTO);

        ResponseEntity<SalesDTO> response = salesController.createSales(salesDTO, validToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(salesDTO.getId(), response.getBody().getId());
        verify(salesService, times(1)).createSales(any(SalesDTO.class));
    }

    @Test
    void obtenerTodasLasVentas_ConTokenValido_DeberiaRetornarLista() {
        List<SalesDTO> ventas = Arrays.asList(salesDTO);
        when(salesService.getAllSales()).thenReturn(ventas);

        ResponseEntity<List<SalesDTO>> response = salesController.getAllSales(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void obtenerTodasLasVentas_ConTokenInvalido_DeberiaRetornarNoAutorizado() {
        ResponseEntity<List<SalesDTO>> response = salesController.getAllSales(invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void obtenerVentaPorId_ConTokenValido_DeberiaRetornarVenta() {
        when(salesService.getSalesById(1L)).thenReturn(salesDTO);

        ResponseEntity<SalesDTO> response = salesController.getSalesById(1L, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(salesDTO.getId(), response.getBody().getId());
    }

    @Test
    void obtenerVentasPorEmail_DeberiaRetornarLista() {
        List<SalesDTO> ventas = Arrays.asList(salesDTO);
        when(salesService.getSalesByCustomerEmail("juan@example.com")).thenReturn(ventas);

        ResponseEntity<List<SalesDTO>> response = salesController.getSalesByCustomerEmail("juan@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void actualizarEstadoVenta_ConTokenValido_DeberiaRetornarVentaActualizada() {
        when(salesService.updateSalesStatus(1L, "ENTREGADO")).thenReturn(salesDTO);

        ResponseEntity<SalesDTO> response = salesController.updateSalesStatus(
                1L, Map.of("status", "ENTREGADO"), validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cancelarVenta_ConTokenValido_DeberiaRetornarSinContenido() {
        doNothing().when(salesService).cancelSales(1L);

        ResponseEntity<Void> response = salesController.cancelSales(1L, validToken);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(salesService, times(1)).cancelSales(1L);
    }
}