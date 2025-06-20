package com.sales_microservice.sales.persistence;


import com.sales_microservice.sales.entities.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales, Long> {
}