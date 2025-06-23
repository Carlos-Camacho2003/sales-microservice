package com.sales_microservice.integration_tests;


import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.security.auth.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    private final String validToken = "validToken123";
    private final String invalidToken = "invalidToken456";
    private final String bearerToken = "Bearer validToken123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(jwtUtils.validateToken(validToken)).thenReturn(true);
        when(jwtUtils.validateToken(invalidToken)).thenReturn(false);
        when(jwtUtils.extractUsername(validToken)).thenReturn("usuario@test.com");
    }

    @Test
    void validarToken_TokenValido_DeberiaRetornarVerdadero() {
        assertTrue(authService.isValidToken(validToken));
    }

    @Test
    void validarToken_TokenInvalido_DeberiaRetornarFalso() {
        assertFalse(authService.isValidToken(invalidToken));
    }

    @Test
    void validarToken_ConPrefijoBearer_DeberiaLimpiarYValidar() {
        assertTrue(authService.isValidToken(bearerToken));
    }

    @Test
    void obtenerUsuarioDeToken_TokenValido_DeberiaRetornarEmail() {
        String username = authService.getUsernameFromToken(validToken);
        assertEquals("usuario@test.com", username);
    }
}