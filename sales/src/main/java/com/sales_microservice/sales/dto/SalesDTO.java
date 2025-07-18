package com.sales_microservice.sales.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SalesDTO {
    private UUID id;
    private String clienteNombre;
    private String clienteEmail;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private List<SalesDetailDTO> detalles;

    // Constructores
    public SalesDTO() {}

}
