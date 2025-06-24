package com.sales_microservice.sales.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderRequestDto {
    @Data
    @NoArgsConstructor
    public static class OrderRequestDTO {
        private String userEmail;
        private String metodoPago;
        private String deliveryAddress; // Nuevo campo
        private String city;            // Nuevo campo
        private String contactPhone;    // Nuevo campo
        private List<OrderItemRequestDTO> items = new ArrayList<>();

        @Data
        @NoArgsConstructor
        public static class OrderItemRequestDTO {
            private Long productId;
            private Integer cantidad;

            // Constructor con campos para pruebas
            public OrderItemRequestDTO(Long productId, Integer cantidad) {
                this.productId = productId;
                this.cantidad = cantidad;
            }
        }
    }
}
