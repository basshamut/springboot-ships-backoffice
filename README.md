# Prueba Técnica Spring Boot

**Importante:** Utiliza la última versión LTS de Java, Spring Boot, y cualquier otra librería utilizada en el proyecto.

## Descripción

Desarrolla una API utilizando Maven, Spring Boot y Java para el mantenimiento CRUD de naves espaciales de series y películas. La API debe permitir:

- Consultar todas las naves con paginación.
- Consultar una nave por su ID.
- Consultar todas las naves que contengan un valor específico en su nombre (por ejemplo, "wing" devuelve "x-wing").
- Crear una nueva nave.
- Modificar una nave existente.
- Eliminar una nave.
- Realizar pruebas unitarias de al menos una clase.
- Desarrollar un @Aspect que registre en el log cuando se solicite una nave con un ID negativo.
- Gestión centralizada de excepciones.
- Utilizar algún tipo de caché.

## Requisitos

- Las naves deben guardarse en una base de datos, como H2 en memoria.
- Presenta la prueba en un repositorio de Git. No es necesario publicarlo, puedes enviarlo comprimido en un único archivo.

## Opcionales

- Utilizar una librería para el mantenimiento de scripts DDL de base de datos.
- Realizar pruebas de integración.
- Dockerizar la aplicación.
- Documentar la API.
- Implementar seguridad en la API.
- Implementar algún consumer/producer para un broker (RabbitMQ, Kafka, etc).

## Solución

### Autenticación
Se implementó autenticación JWT con Spring Security.

- **Username:** `test@test.com`
- **Password:** `bWlDb250cmFzZcOxYTEyMw==` (El password es `miContraseña123` en Base64)

### Construcción y Ejecución

#### Docker

Construir la imagen Docker:
```bash
docker build -t spring-boot-docker .
```

#### Swagger

La documentación de la API está disponible en:
[Swagger UI](http://localhost:8080/swagger-ui.html)

#### RabbitMQ

Para ejecutar RabbitMQ:

1. Descargar la imagen de RabbitMQ con gestión:
    ```bash
    docker pull rabbitmq:3-management
    ```
2. Ejecutar el contenedor de RabbitMQ:
    ```bash
    docker run -d -p 9090:15672 -p 9091:5672 --name rabbitmq rabbitmq:3-management
    ```
3. Acceder al Dashboard de RabbitMQ:
   [RabbitMQ Dashboard](http://localhost:9090)

