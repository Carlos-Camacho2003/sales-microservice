package com.sales_microservice.sales.infraestructure.clients.products;

import com.sales_microservice.sales.dto.external.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-microservice")
public interface ProductServiceClient {
    @GetMapping("/product/all")
    List<ProductDto> getAllProducts();
}