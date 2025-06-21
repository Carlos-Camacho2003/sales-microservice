package com.sales_microservice.sales.service;

import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.entities.Sales;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface ISalesService {
    SalesDTO createSales(SalesDTO salesDTO);
    SalesDTO getSalesById(Long id);
    List<SalesDTO> getAllSales();
    List<SalesDTO> getSalesByCustomerEmail(String email);
    SalesDTO updateSalesStatus(Long id, String status);
    void cancelSales(Long id);
}
