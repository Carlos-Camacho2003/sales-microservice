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

