package com.sales_microservice.unit_tests;
import com.sales_microservice.sales.exception.InvalidSalesException;
import com.sales_microservice.sales.exception.SalesNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcepcionTest {

    @Test
    void cuandoCrearSalesNotFoundException_deberiaContenerMensajeCorrecto() {
        // Mensaje esperado
        String mensajeEsperado = "Venta no encontrada con ID: 999";

        // Crear excepción
        SalesNotFoundException excepcion = new SalesNotFoundException(mensajeEsperado);

        // Verificar
        assertEquals(mensajeEsperado, excepcion.getMessage());
    }

    @Test
    void cuandoCrearInvalidSalesException_deberiaContenerMensajeCorrecto() {
        // Mensaje esperado
        String mensajeEsperado = "La venta no es válida";

        // Crear excepción
        InvalidSalesException excepcion = new InvalidSalesException(mensajeEsperado);

        // Verificar
        assertEquals(mensajeEsperado, excepcion.getMessage());
    }
}

