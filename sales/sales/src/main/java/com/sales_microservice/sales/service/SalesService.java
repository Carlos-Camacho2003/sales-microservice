package com.sales_microservice.sales.service;

import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.persistence.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    @Transactional
    public Sales createOrder(Sales order) {
        order.setFecha(LocalDateTime.now());
        order.setEstado("PENDIENTE");

        // Calcular total
        BigDecimal total = order.getDetalles().stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        // Guardar orden y detalles
        return salesRepository.save(order);
    }

    public List<Sales> getAllOrders() {
        return salesRepository.findAll();
    }

    public Sales getOrderById(Long id) {
        return salesRepository.findById(id).orElse(null);
    }

    @Transactional
    public Sales updateOrderStatus(Long id, String newStatus) {
        Sales sales = salesRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Orden no encontrada"));
        sales.setEstado(newStatus);
        return salesRepository.save(sales);
    }
}
