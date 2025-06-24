package com.sales_microservice.sales.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    private UUID id;
    private UUID userId;
    private String clienteNombre;
    private String clienteEmail;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private List<OrderItemResponseDTO> items;

    @Data
    public static class OrderItemResponseDTO {
        private Long id;
        private Long productId;
        private String productName;  // Obtenido de micro-productos
        private Integer cantidad;
        private BigDecimal unitPrice;  // Precio en el momento de la compra
        private BigDecimal subtotal;
    }
}

