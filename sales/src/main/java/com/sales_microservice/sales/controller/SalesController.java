package com.sales_microservice.sales.controller;

import com.sales_microservice.sales.dto.OrderRequestDto;
import com.sales_microservice.sales.dto.OrderResponseDTO;
import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.service.ISalesService;
import com.sales_microservice.sales.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final ISalesService salesService;
    private final AuthService authService;
    private final OrderService orderService;

    @Autowired
    public SalesController(ISalesService salesService,
                           AuthService authService,
                           OrderService orderService)
    {
        this.salesService = salesService;
        this.authService = authService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createSales(
            @RequestBody OrderRequestDto.OrderRequestDTO orderRequest,
            @RequestHeader("Authorization") String token) {

        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else {
            OrderResponseDTO createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<SalesDTO>> getAllSales(
            @RequestHeader("Authorization") String token)
    {
        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else{
            List<SalesDTO> sales = salesService.getAllSales();
            return ResponseEntity.ok(sales);
        }

    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SalesDTO> getSalesById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token)
    {
        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else {
            SalesDTO sale = salesService.getSalesById(id);
            return ResponseEntity.ok(sale);
        }

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<SalesDTO>> getSalesByCustomerEmail(@PathVariable String email) {
        List<SalesDTO> sales = salesService.getSalesByCustomerEmail(email);
        return ResponseEntity.ok(sales);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<SalesDTO> updateSalesStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {

        String status = request.get("status");
        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else{
            SalesDTO updatedSale = salesService.updateSalesStatus(id, status);
            return ResponseEntity.ok(updatedSale);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> cancelSales(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else{
            salesService.cancelSales(id);
            return ResponseEntity.noContent().build();
        }

    }
}