package com.sales_microservice.sales.controller;

import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.service.ISalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final ISalesService salesService;

    @Autowired
    public SalesController(ISalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping("/create")
    public ResponseEntity<SalesDTO> createSales(@RequestBody SalesDTO salesDTO) {
        SalesDTO createdSale = salesService.createSales(salesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @GetMapping("/get")
    public ResponseEntity<List<SalesDTO>> getAllSales() {
        List<SalesDTO> sales = salesService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SalesDTO> getSalesById(@PathVariable Long id) {
        SalesDTO sale = salesService.getSalesById(id);
        return ResponseEntity.ok(sale);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<SalesDTO>> getSalesByCustomerEmail(@PathVariable String email) {
        List<SalesDTO> sales = salesService.getSalesByCustomerEmail(email);
        return ResponseEntity.ok(sales);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<SalesDTO> updateSalesStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        SalesDTO updatedSale = salesService.updateSalesStatus(id, status);
        return ResponseEntity.ok(updatedSale);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> cancelSales(@PathVariable Long id) {
        salesService.cancelSales(id);
        return ResponseEntity.noContent().build();
    }
}