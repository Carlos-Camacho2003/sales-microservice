package com.sales_microservice.sales.service;

import com.sales_microservice.sales.dto.OrderRequestDto;
import com.sales_microservice.sales.dto.OrderResponseDTO;
import com.sales_microservice.sales.dto.external.DeliveryCreationRequest;
import com.sales_microservice.sales.dto.external.UserDto;
import com.sales_microservice.sales.dto.external.ProductDto;
import com.sales_microservice.sales.entities.Sales;
import com.sales_microservice.sales.entities.SalesDetail;
import com.sales_microservice.sales.infraestructure.clients.delivery.DeliveriesServiceClient;
import com.sales_microservice.sales.infraestructure.clients.products.ProductServiceClient;
import com.sales_microservice.sales.infraestructure.clients.users.UserServiceClient;
import com.sales_microservice.sales.persistence.SalesRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;
    private final ProductServiceClient productServiceClient;
    private final SalesRepository salesRepository;
    private final DeliveriesServiceClient deliveriesServiceClient; // Inyectar

    @Autowired
    public OrderService(UserServiceClient userServiceClient,
                        ProductServiceClient productServiceClient,
                        SalesRepository salesRepository,
                        DeliveriesServiceClient deliveriesServiceClient)
    {
        this.userServiceClient = userServiceClient;
        this.productServiceClient = productServiceClient;
        this.salesRepository = salesRepository;
        this.deliveriesServiceClient = deliveriesServiceClient;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDto.OrderRequestDTO request) {
        // 1. Obtener y validar usuario
        UserDto user = userServiceClient.getUserByEmail(request.getUserEmail());
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // 2. Obtener TODOS los productos
        List<ProductDto> allProducts = productServiceClient.getAllProducts();

        // 3. Extraer los IDs solicitados
        List<Long> productIds = request.getItems().stream()
                .map(OrderRequestDto.OrderRequestDTO.OrderItemRequestDTO::getProductId)
                .collect(Collectors.toList());

        // 4. Crear mapa para búsqueda rápida
        Map<Long, ProductDto> productMap = allProducts.stream()
                .collect(Collectors.toMap(ProductDto::getId, Function.identity()));

        // 5. Calcular total y crear detalles
        BigDecimal total = BigDecimal.ZERO;
        List<SalesDetail> detalles = new ArrayList<>();

        for (OrderRequestDto.OrderRequestDTO.OrderItemRequestDTO item : request.getItems()) {
            ProductDto product = productMap.get(item.getProductId());
            if (product == null) {
                throw new RuntimeException("Producto no encontrado: " + item.getProductId());
            }

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(item.getCantidad()));
            total = total.add(subtotal);

            SalesDetail detail = new SalesDetail();
            detail.setProductoId(product.getId());
            detail.setProductoNombre(product.getName());
            detail.setCantidad(item.getCantidad());
            detail.setPrecioUnitario(unitPrice);
            detail.setSubtotal(subtotal);

            detalles.add(detail);
        }

        // 4. Crear y guardar la orden
        Sales sales = new Sales();
        sales.setUserId(user.getId());
        sales.setClienteNombre(user.getFirstName() + " " + user.getLastName());
        sales.setClienteEmail(user.getEmail());
        sales.setFecha(LocalDateTime.now());
        sales.setTotal(total);
        sales.setMetodoPago(request.getMetodoPago());
        sales.setEstado("PENDIENTE");
        sales.setDetalles(detalles);

        // Establecer relación bidireccional
        detalles.forEach(detail -> detail.setSales(sales));

        Sales savedSales = salesRepository.save(sales);
        List<OrderResponseDTO.OrderItemResponseDTO> itemsDTO = savedSales.getDetalles().stream()
                .map(this::convertDetailToDTO)
                .collect(Collectors.toList());
        /*
         tira pa' que yo la pruebe
            Se pone olorosa y me gusta cómo huele (Cómo huele)
            Instagram privado, pa' que nadie la vele
            Se puso bonita porque sabe que hoy se bebe
            A portarse mal pa' sentirse bien
            No quería fumar, pero le dio al pen (Sí)
            Una Barbie, pero no busca un Ken (No)
            Siempre le llego cuando dice "Ven"
            Pa' portarse mal se viste bien
            Dice la verdad y a vece' miente también
            Apaga las notificacione' en el cel (Cel)
            Ya tiene lo suyo, pero hoy quiere joder y
            Yo le di por el expreso
            Le llené el cuello de beso'
            Le hice tiempo como un preso
            Si la ve', no le hable' de eso, que
            Se hace la que no me conoce (No me conoce)
            Pero, en mi cama, se volvió un vicio como la cinco doce (Como la cinco doce)
            Me la como entera y nadie se entera
            Un par de amiga' (Un par de amiga')
            Toda' solteras, siempre la velan pa' que ella siga (Siga)
         */
        createDeliveryRecord(savedSales.getId(), request);

        return convertToResponseDTO(savedSales, itemsDTO);

    }

    private void createDeliveryRecord(UUID orderId, OrderRequestDto.OrderRequestDTO request) {
        try {
            System.out.println("Intentando crear delivery para orden: " + orderId);


            DeliveryCreationRequest deliveryRequest = new DeliveryCreationRequest();
            deliveryRequest.setOrderId(orderId.toString());
            deliveryRequest.setDeliveryAddress(request.getDeliveryAddress());
            deliveryRequest.setCity(request.getCity());
            deliveryRequest.setContactPhone(request.getContactPhone());

            // Agrega log del request
            System.out.println("Request a delivery: " + deliveryRequest);

            deliveriesServiceClient.createDelivery(deliveryRequest);
            System.out.println("Delivery creado exitosamente");
        } catch (Exception e) {
            System.err.println("Error creando delivery: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private OrderResponseDTO convertToResponseDTO(Sales orderEntity, List<OrderResponseDTO.OrderItemResponseDTO> orderItems) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();

        responseDTO.setId(orderEntity.getId());
        responseDTO.setUserId(orderEntity.getUserId());
        responseDTO.setClienteNombre(orderEntity.getClienteNombre());
        responseDTO.setClienteEmail(orderEntity.getClienteEmail());
        responseDTO.setFecha(orderEntity.getFecha());
        responseDTO.setTotal(orderEntity.getTotal());
        responseDTO.setMetodoPago(orderEntity.getMetodoPago());
        responseDTO.setEstado(orderEntity.getEstado());
        responseDTO.setItems(orderItems);

        return responseDTO;
    }

    private OrderResponseDTO.OrderItemResponseDTO convertDetailToDTO(SalesDetail detail) {
        OrderResponseDTO.OrderItemResponseDTO itemDTO = new OrderResponseDTO.OrderItemResponseDTO();
        itemDTO.setId(detail.getId());
        itemDTO.setProductId(detail.getProductoId());
        itemDTO.setProductName(detail.getProductoNombre());
        itemDTO.setCantidad(detail.getCantidad());
        itemDTO.setUnitPrice(detail.getPrecioUnitario());
        itemDTO.setSubtotal(detail.getSubtotal());
        return itemDTO;
    }
}

