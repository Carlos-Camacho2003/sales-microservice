package com.sales_microservice.sales.dto.external;


import lombok.Data;

@Data
public class DeliveryCreationRequest {
    private String orderId;
    private String deliveryAddress;
    private String city;
    private String contactPhone;
}