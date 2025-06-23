package com.sales_microservice.unit_tests;


import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.security.auth.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    void cuandoTokenEsValido_deberiaRetornarVerdadero() {
        // Configuración
        String token = "tokenValido";
        when(jwtUtils.validateToken(token)).thenReturn(true);

        // Ejecución
        boolean resultado = authService.isValidToken("Bearer " + token);

        // Verificación
        assertTrue(resultado);
        verify(jwtUtils, times(1)).validateToken(token);
    }

    @Test
    void cuandoTokenEsInvalido_deberiaRetornarFalso() {
        // Configuración
        String token = "tokenInvalido";
        when(jwtUtils.validateToken(token)).thenReturn(false);

        // Ejecución
        boolean resultado = authService.isValidToken("Bearer " + token);

        // Verificación
        assertFalse(resultado);
    }

    @Test
    void cuandoObtenerUsername_deberiaExtraerUsernameCorrectamente() {
        // Configuración
        String token = "tokenValido";
        String usernameEsperado = "usuario@test.com";
        when(jwtUtils.extractUsername(token)).thenReturn(usernameEsperado);

        // Ejecución
        String usernameResultado = authService.getUsernameFromToken("Bearer " + token);

        // Verificación
        assertEquals(usernameEsperado, usernameResultado);
    }
}