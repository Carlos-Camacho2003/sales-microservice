package com.sales_microservice.sales.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesDetailDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    // Constructores
    public SalesDetailDTO() {}


    // ... (generar los demás getters y setters)
}
