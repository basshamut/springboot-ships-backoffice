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
Se implementó autenticación básica con Spring Security.

- **Username:** `test@test.com`
- **Password:** `bWlDb250cmFzZcOxYTEyMw==` (El password es `miContraseña123` en Base64)

### Construcción y Ejecución

#### Requisitos Previos
- Java 21
- Maven
- Docker y Docker Compose

#### Ambiente de Desarrollo con Docker Compose

El proyecto incluye un ambiente de desarrollo completo usando Docker Compose que incluye:
- La aplicación Spring Boot
- RabbitMQ con interfaz de gestión
- Base de datos H2 en memoria

Para iniciar el ambiente:

1. Construir el proyecto:
```bash
mvn clean package
```

2. Iniciar los servicios:
```bash
docker-compose up -d
```

3. Ver los logs:
```bash
docker-compose logs -f
```

4. Detener los servicios:
```bash
docker-compose down
```

#### Accesos a los Servicios

- **API REST:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **RabbitMQ Management:** http://localhost:15672
  - Usuario: `guest`
  - Contraseña: `guest`

#### Desarrollo Local (sin Docker)

Si prefieres ejecutar la aplicación sin Docker:

1. Construir el proyecto:
```bash
mvn clean package
```

2. Ejecutar la aplicación:
```bash
java -jar target/springboot-ships-backoffice-1.0-SNAPSHOT.jar
```

#### RabbitMQ (Desarrollo Local)

Si necesitas ejecutar RabbitMQ localmente sin Docker Compose:

1. Descargar la imagen de RabbitMQ con gestión:
```bash
docker pull rabbitmq:3-management
```

2. Ejecutar el contenedor de RabbitMQ:
```bash
docker run -d -p 9090:15672 -p 9091:5672 --name rabbitmq rabbitmq:3-management
```

3. Acceder al Dashboard de RabbitMQ:
   [RabbitMQ Dashboard](http://localhost:15672)

### Características Implementadas

- ✅ CRUD completo de naves espaciales
- ✅ Paginación en consultas
- ✅ Búsqueda por nombre
- ✅ Pruebas unitarias
- ✅ Aspect para logging de IDs negativos
- ✅ Gestión centralizada de excepciones
- ✅ Caché con Caffeine
- ✅ Base de datos H2 en memoria
- ✅ Flyway para migraciones de base de datos
- ✅ Documentación con Swagger/OpenAPI
- ✅ Autenticación con Spring Security
- ✅ Integración con RabbitMQ
- ✅ Docker y Docker Compose
- ✅ Pruebas de integración con Cucumber

