package com.sales_microservice.sales.dto.external;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private double price;
}
