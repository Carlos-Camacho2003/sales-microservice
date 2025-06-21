# README - Microservicio de Ventas (Sales Service)

## 📌 Descripción del Proyecto

Microservicio Spring Boot para la gestión de ventas y órdenes de compra, con autenticación basada en roles y conexión a base de datos PostgreSQL.

## 🛠 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL** (Alojado en Neon.tech)
- **Maven** (Gestión de dependencias)

## 🔐 Autenticación y Roles

El sistema utiliza autenticación Basic Auth con los siguientes usuarios predefinidos:

| Usuario   | Contraseña | Rol        | Acceso                                  |
|-----------|-------|------------|-----------------------------------------|
| admin     | 123   | ADMIN      | Todas las operaciones                   |
| client    | 123 | CLIENT     | Crear/consultar sus ventas              |
| deliverer | 123 | DELIVERER  | Consultar ventas y actualizar estados   |

## 🌐 Endpoints Disponibles

### 1. Crear Venta
- **Método**: POST
- **Ruta**: `/sales/create`
- **Roles**: ADMIN, CLIENT
- **Body** (Ejemplo):
```json
{
    "clienteNombre": "Juan Pérez",
    "clienteEmail": "juan@example.com",
    "metodoPago": "Tarjeta",
    "detalles": [
        {
            "productoId": 101,
            "productoNombre": "Laptop",
            "cantidad": 1,
            "precioUnitario": 1200.00
        }
    ]
}
```
## Endpoints de Ventas

### 2. Obtener Todas las Ventas
- **Método:** `GET`
- **Ruta:** `/sales/get`
- **Roles autorizados:** `ADMIN`, `CLIENT`, `DELIVERER`

---

### 3. Obtener Venta por ID
- **Método:** `GET`
- **Ruta:** `/sales/get/{id}`
- **Roles autorizados:** `ADMIN`, `CLIENT`, `DELIVERER`

---

### 4. Obtener Ventas por Email
- **Método:** `GET`
- **Ruta:** `/sales/email/{email}`
- **Roles autorizados:** `ADMIN`, `CLIENT`

---

### 5. Actualizar Estado de Venta
- **Método:** `PATCH`
- **Ruta:** `/sales/update/{id}?status=ENVIADO`
- **Roles autorizados:** `ADMIN`
- **Estados disponibles:** `PENDIENTE`, `ENVIADO`, `ENTREGADO`, `CANCELADO`

---

### 6. Cancelar Venta
- **Método:** `DELETE`
- **Ruta:** `/sales/delete/{id}`
- **Roles autorizados:** `ADMIN`

---

## 🚀 Configuración del Proyecto

### Requisitos Previos
- Java 17 JDK instalado
- Maven 3.8 o superior
- Acceso a la base de datos PostgreSQL

## 📊 Estructura del Proyecto

```
src/
├── main/
│ ├── java/
│ │ └── com/sales_microservice/
│ │ ├── controller/ # Controladores REST
│ │ ├── entities/ # Entidades JPA
│ │ ├── repositories/ # Repositorios Spring Data
│ │ ├── service/ # Lógica de negocio
│ │ ├── dto/ # Objetos de transferencia
│ │ └── security/ # Configuración de seguridad
│ └── resources/
│ └── application.properties
```
## 🔄 Flujo de Trabajo Típico

1. **Cliente crea una venta**  
   `POST /sales/create`

2. **Administrador consulta todas las ventas**  
   `GET /sales/get`

3. **Repartidor actualiza estado a "ENVIADO"**  
   `PATCH /sales/update/1?status=ENVIADO`

4. **Cliente consulta sus ventas por email**  
   `GET /sales/email/cliente@test.com`

5. **Administrador cancela una venta si es necesario**  
   `DELETE /sales/delete/1`

---

## 📝 Notas Adicionales

- La base de datos se auto-configura con:  
  `spring.jpa.hibernate.ddl-auto=update`

- Todos los endpoints requieren autenticación **Basic Auth**

- Las respuestas siguen el formato estándar de **Spring Boot**

