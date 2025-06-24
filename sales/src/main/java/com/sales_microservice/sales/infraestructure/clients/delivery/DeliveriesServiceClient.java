    package com.sales_microservice.sales.infraestructure.clients.delivery;

    import com.sales_microservice.sales.dto.external.DeliveryCreationRequest;
    import org.springframework.cloud.openfeign.FeignClient;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;

    @FeignClient(name = "delivery-microservice")
    public interface DeliveriesServiceClient {
        @PostMapping("/api/deliveries")
        void createDelivery(@RequestBody DeliveryCreationRequest request);
    }
