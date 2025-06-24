package com.sales_microservice.sales.controller;

import com.sales_microservice.sales.dto.OrderRequestDto;
import com.sales_microservice.sales.dto.OrderResponseDTO;
import com.sales_microservice.sales.dto.SalesDTO;
import com.sales_microservice.sales.security.auth.AuthService;
import com.sales_microservice.sales.service.ISalesService;
import com.sales_microservice.sales.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Ventas", description = "Operaciones relacionadas con la gestión de ventas")
@RestController
@RequestMapping("/sales")
public class SalesController {

    private final ISalesService salesService;
    private final AuthService authService;
    private final OrderService orderService;

    @Autowired
    public SalesController(ISalesService salesService,
                           AuthService authService,
                           OrderService orderService)
    {
        this.salesService = salesService;
        this.authService = authService;
        this.orderService = orderService;
    }

    @Operation(
            summary = "Crear una nueva venta",
            description = "Crea una nueva orden de venta",
            security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la venta a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SalesDTO.class),
                            examples = @ExampleObject(
                                    value = "{\"clienteNombre\":\"Juan Pérez\",\"clienteEmail\":\"juan@example.com\",\"metodoPago\":\"Tarjeta\",\"detalles\":[{\"productoId\":101,\"productoNombre\":\"Laptop\",\"cantidad\":1,\"precioUnitario\":1200.00}]}"
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o faltante"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado")
    })
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createSales(
            @RequestBody OrderRequestDto.OrderRequestDTO orderRequest,
            @RequestHeader("Authorization") String token) {
        boolean isValidToken = authService.isValidToken(token);

        if(!isValidToken){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }else {
            OrderResponseDTO createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        }
    }

    @Operation(
            summary = "Obtener todas las ventas",
            description = "Retorna una lista de todas las ventas registradas",
            security = @SecurityRequirement(name = "JWT")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o faltante"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado")
    })
    @GetMapping("/get")
    public ResponseEntity<List<SalesDTO>> getAllSales(
            @RequestHeader("Authorization") String token) {
        boolean isValidToken = authService.isValidToken(token);
        if(!isValidToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            List<SalesDTO> sales = salesService.getAllSales();
            return ResponseEntity.ok(sales);
        }
    }

    @Operation(
            summary = "Obtener venta por ID",
            description = "Retorna una venta específica según su ID",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(name = "id", description = "ID de la venta", required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o faltante"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<SalesDTO> getSalesById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        boolean isValidToken = authService.isValidToken(token);
        if(!isValidToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            SalesDTO sale = salesService.getSalesById(id);
            return ResponseEntity.ok(sale);
        }
    }

    @Operation(
            summary = "Obtener ventas por email de cliente",
            description = "Retorna todas las ventas asociadas a un email de cliente",
            parameters = {
                    @Parameter(name = "email", description = "Email del cliente", required = true, example = "cliente@example.com")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron ventas para este email")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<List<SalesDTO>> getSalesByCustomerEmail(@PathVariable String email) {
        List<SalesDTO> sales = salesService.getSalesByCustomerEmail(email);
        return ResponseEntity.ok(sales);
    }

    @Operation(
            summary = "Actualizar estado de una venta",
            description = "Actualiza el estado de una venta existente",
            security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo estado de la venta",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(
                                    value = "{\"status\":\"ENVIADO\"}"
                            )
                    )
            ),
            parameters = {
                    @Parameter(name = "id", description = "ID de la venta a actualizar", required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o faltante"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<SalesDTO> updateSalesStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String token) {
        String status = request.get("status");
        boolean isValidToken = authService.isValidToken(token);
        if(!isValidToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            SalesDTO updatedSale = salesService.updateSalesStatus(id, status);
            return ResponseEntity.ok(updatedSale);
        }
    }

    @Operation(
            summary = "Cancelar una venta",
            description = "Cancela una venta existente",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(name = "id", description = "ID de la venta a cancelar", required = true, example = "1")
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venta cancelada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o faltante"),
            @ApiResponse(responseCode = "403", description = "Prohibido - Rol no autorizado o venta no cancelable"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> cancelSales(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        boolean isValidToken = authService.isValidToken(token);
        if(!isValidToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            salesService.cancelSales(id);
            return ResponseEntity.noContent().build();
        }
    }
}