package com.sales_microservice.sales.persistence;

import com.sales_microservice.sales.entities.SalesDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesDetailRepository extends JpaRepository<SalesDetail, Long> {
}