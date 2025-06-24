# 💰 Microservicio de Ventas (Sales Microservice)

Este microservicio se encarga de la gestión completa del ciclo de ventas, incluyendo creación, consulta, actualización y cancelación de ventas. Está desarrollado con **Spring Boot** y forma parte de una arquitectura basada en microservicios. Utiliza autenticación JWT y se conecta a una base de datos PostgreSQL para almacenar la información.

## 📦 Tecnologías

- Java 17+
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- Docker
- Lombok

## ⚙️ Configuración

Asegúrate de definir las siguientes variables de entorno:

| Variable  | Descripción                            | Ejemplo                                                        |
|-----------|----------------------------------------|----------------------------------------------------------------|
| DB_URL    | URL de conexión a la base de datos     | jdbc:postgresql://ep-xyz.us-east-2.aws.neon.tech/salesdb       |

La base URL por defecto del microservicio es:  

http://localhost:8080  


## 🚀 Endpoints principales

| Método | Endpoint               | Descripción                                |
|--------|------------------------|--------------------------------------------|
| POST   | /sales/create          | Crea una nueva venta                        |
| GET    | /sales/get             | Obtiene todas las ventas                    |
| GET    | /sales/get/{id}        | Obtiene una venta específica por ID         |
| GET    | /sales/email/{email}   | Obtiene ventas por email de cliente         |
| PATCH  | /sales/update/{id}     | Actualiza el estado de una venta            |
| DELETE | /sales/delete/{id}     | Cancela una venta                           |

## 🛡️ Seguridad

Este microservicio implementa autenticación mediante **JWT**. Todos los endpoints (excepto `/sales/email/{email}`) requieren un token válido.

- Los tokens deben enviarse en la cabecera HTTP:

