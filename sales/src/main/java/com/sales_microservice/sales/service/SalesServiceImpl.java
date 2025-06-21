package com.sales_microservice.sales.service;

import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.entities.SalesDetail;
import com.sales_microservice.sales.entities.SalesStatus;
import com.sales_microservice.sales.exception.InvalidSalesException;
import com.sales_microservice.sales.exception.SalesNotFoundException;
import com.sales_microservice.sales.mapper.SalesMapper;
import com.sales_microservice.sales.persistence.SalesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements ISalesService {

    private final SalesRepository salesRepository;
    private final SalesMapper salesMapper;

    @Autowired
    public SalesServiceImpl(SalesRepository salesRepository, SalesMapper salesMapper) {
        this.salesRepository = salesRepository;
        this.salesMapper = salesMapper;
    }

    @Override
    @Transactional
    public SalesDTO createSales(SalesDTO salesDTO) {
        if (salesDTO == null || salesDTO.getDetalles() == null || salesDTO.getDetalles().isEmpty()) {
            throw new InvalidSalesException("La venta y sus detalles no pueden ser nulos");
        }

        // Convertir DTO a entidad
        Sales sales = new Sales();
        sales.setClienteNombre(salesDTO.getClienteNombre());
        sales.setClienteEmail(salesDTO.getClienteEmail());
        sales.setFecha(LocalDateTime.now());
        sales.setEstado(SalesStatus.PENDIENTE.name());
        sales.setMetodoPago(salesDTO.getMetodoPago());

        // Convertir y asignar detalles
        List<SalesDetail> detalles = salesDTO.getDetalles().stream()
                .map(detailDTO -> {
                    SalesDetail detail = new SalesDetail();
                    detail.setProductoId(detailDTO.getProductoId());
                    detail.setProductoNombre(detailDTO.getProductoNombre());
                    detail.setCantidad(detailDTO.getCantidad());
                    detail.setPrecioUnitario(detailDTO.getPrecioUnitario());
                    detail.setSales(sales); // Establecer relación bidireccional
                    return detail;
                })
                .collect(Collectors.toList());

        sales.setDetalles(detalles);

        calculateSalesTotals(sales);
        validateProductAvailability(sales);

        Sales savedSales = salesRepository.save(sales);
        return salesMapper.toDto(savedSales);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesDTO getSalesById(Long id) {
        Sales sales = salesRepository.findById(id)
                .orElseThrow(() -> new SalesNotFoundException("Venta no encontrada con ID: " + id));
        return salesMapper.toDto(sales);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesDTO> getAllSales() {
        return salesRepository.findAll().stream()
                .map(salesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesDTO> getSalesByCustomerEmail(String email) {
        return salesRepository.findByClienteEmail(email).stream()
                .map(salesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalesDTO updateSalesStatus(Long id, String status) {
        Sales sales = salesRepository.findById(id)
                .orElseThrow(() -> new SalesNotFoundException("Venta no encontrada con ID: " + id));

        try {
            SalesStatus newStatus = SalesStatus.valueOf(status.toUpperCase());
            sales.setEstado(newStatus.name());
            Sales updatedSales = salesRepository.save(sales);
            return salesMapper.toDto(updatedSales);
        } catch (IllegalArgumentException e) {
            throw new InvalidSalesException("Estado de venta no válido: " + status);
        }
    }

    @Override
    @Transactional
    public void cancelSales(Long id) {
        Sales sales = salesRepository.findById(id)
                .orElseThrow(() -> new SalesNotFoundException("Venta no encontrada con ID: " + id));

        if (sales.getEstado().equals(SalesStatus.ENVIADO.name())) {
            throw new InvalidSalesException("No se puede cancelar una venta ya enviada");
        }

        sales.setEstado(SalesStatus.CANCELADO.name());
        salesRepository.save(sales);
    }

    private void calculateSalesTotals(Sales sales) {
        BigDecimal total = BigDecimal.ZERO;

        for (SalesDetail detail : sales.getDetalles()) {
            BigDecimal subtotal = detail.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detail.getCantidad()));
            detail.setSubtotal(subtotal);
            total = total.add(subtotal);
        }

        sales.setTotal(total);
    }

    private void validateProductAvailability(Sales sales) {
        sales.getDetalles().forEach(detail -> {
            if (detail.getCantidad() <= 0) {
                throw new InvalidSalesException(
                        "Cantidad inválida para el producto: " + detail.getProductoNombre());
            }
        });
    }
}