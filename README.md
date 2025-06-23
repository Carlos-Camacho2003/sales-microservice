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

# Guía Completa de las  Pruebas  de Cobertura

Este proyecto utiliza **JaCoCo** para generar reportes de cobertura de pruebas. A continuación
se explican los pasos necesarios para ejecutar las pruebas y visualizar el reporte generado.

---

### 🔹 Paso 1: Limpiar el proyecto y ejecutar pruebas
Estar posicionado en esta ruta
>\sales-microservice\sales>
> mvn clean test

Este comando realiza una limpieza del proyecto y luego ejecuta todas las pruebas configuradas:

- clean: Elimina archivos temporales y la carpeta target para asegurar una compilación limpia.

- test: Ejecuta todas las pruebas unitarias e integrales definidas en el proyecto.

### 🔹  Paso 2: Preparar JaCoCo e instalar el proyecto

> mvn org.jacoco:jacoco-maven-plugin:0.8.11:prepare-agent install

Este comando configura el agente de JaCoCo y compila el proyecto:

- prepare-agent: Inicializa el agente de JaCoCo que recolectará los datos de cobertura.

- install: Compila e instala el proyecto en el repositorio local de Maven,
  ejecutando nuevamente las pruebas con el agente activado.

### 🔹 Paso 3: Generar el reporte de cobertura
> mvn org.jacoco:jacoco-maven-plugin:0.8.11:report

Este comando genera un informe detallado de cobertura de código basado en las pruebas ejecutadas anteriormente.

### 🔹📊 Visualizar el reporte de cobertura

1. Una vez ejecutados los comandos anteriores, navega hasta la
   siguiente ruta del proyecto:

target/site/jacoco/

2. Dentro de esta carpeta, ubica el archivo:

index.html

3. Arrastra este archivo a tu navegador o ábrelo directamente con doble clic.

Esto te permitirá visualizar un informe completo con métricas como cobertura por clase,
línea, rama, etc., generado por JaCoCo.

