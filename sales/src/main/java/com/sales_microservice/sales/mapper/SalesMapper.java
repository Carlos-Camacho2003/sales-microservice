package com.sales_microservice.sales.mapper;

import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.dto.SalesDetailDTO;
import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.entities.SalesDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesMapper {

    public SalesDTO toDto(Sales sales) {
        SalesDTO dto = new SalesDTO();
        dto.setId(sales.getId());
        dto.setClienteNombre(sales.getClienteNombre());
        dto.setClienteEmail(sales.getClienteEmail());
        dto.setFecha(sales.getFecha());
        dto.setTotal(sales.getTotal());
        dto.setMetodoPago(sales.getMetodoPago());
        dto.setEstado(sales.getEstado());

        if (sales.getDetalles() != null) {
            List<SalesDetailDTO> detallesDTO = sales.getDetalles().stream()
                    .map(this::toDetailDto)
                    .collect(Collectors.toList());
            dto.setDetalles(detallesDTO);
        }

        return dto;
    }

    public SalesDetailDTO toDetailDto(SalesDetail detail) {
        SalesDetailDTO dto = new SalesDetailDTO();
        dto.setId(detail.getId());
        dto.setProductoId(detail.getProductoId());
        dto.setProductoNombre(detail.getProductoNombre());
        dto.setCantidad(detail.getCantidad());
        dto.setPrecioUnitario(detail.getPrecioUnitario());
        dto.setSubtotal(detail.getSubtotal());
        return dto;
    }
}
