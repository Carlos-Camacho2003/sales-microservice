package com.sales_microservice.sales.controller;

import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.service.SalesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping
    public ResponseEntity<Sales> createOrder(@RequestBody Sales sales) {
        return ResponseEntity.ok(salesService.createOrder(sales));
    }

    @GetMapping
    public ResponseEntity<List<Sales>> getAllOrders() {
        return ResponseEntity.ok(salesService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sales> getOrderById(@PathVariable Long id) {
        Sales sales = salesService.getOrderById(id);
        if (sales != null) {
            return ResponseEntity.ok(sales);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Sales> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(salesService.updateOrderStatus(id, status));
    }
}
