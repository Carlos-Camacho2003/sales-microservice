package com.sales_microservice.sales.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        //holaaa 3
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Ventas")
                        .version("1.0")
                        .description("API para gestión de ventas y órdenes de compra")
                        .contact(new Contact()
                                .name("Harrison Ineey valencia Otero")
                                .email("harrison.valencia@correounivalle.edu.co"))
                        .license(new License()
                                .name("Apache 2.0")))
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtenido del endpoint de autenticación")));
    }
}
