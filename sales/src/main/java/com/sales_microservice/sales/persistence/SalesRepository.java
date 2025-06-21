package com.sales_microservice.sales.persistence;


import com.sales_microservice.sales.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {
    List<Sales> findByClienteEmail(String email);
}