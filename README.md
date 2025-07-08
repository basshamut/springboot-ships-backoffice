# API de Gestión de Naves Espaciales

Este proyecto implementa una API RESTful completa para la gestión de naves espaciales de películas y series, desarrollada con Spring Boot y Java 21.

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Seguridad y Autenticación](#seguridad-y-autenticación)
- [Documentación de la API](#documentación-de-la-api)
- [Integración con Sistemas de Mensajería](#integración-con-sistemas-de-mensajería)
- [Base de Datos](#base-de-datos)
- [Pruebas](#pruebas)

## Características

El sistema implementa todas las operaciones CRUD para naves espaciales:

- **Consultas avanzadas**:
  - Listado completo con paginación y ordenamiento
  - Búsqueda por ID
  - Búsqueda por nombre (parcial o completo)
  - Filtrado por múltiples criterios

- **Operaciones de modificación**:
  - Creación de nuevas naves
  - Actualización de naves existentes
  - Eliminación de naves

- **Características avanzadas**:
  - Caché para optimizar rendimiento
  - Gestión centralizada de excepciones
  - Logging mediante aspectos AOP
  - Validación de datos de entrada
  - Mensajería asíncrona con RabbitMQ y Kafka

## Tecnologías

- **Backend**: Java 21, Spring Boot 3.x
- **Base de datos**: H2 (embebida)
- **Migración de datos**: Flyway
- **Seguridad**: Spring Security con JWT
- **Documentación**: OpenAPI (Swagger)
- **Mensajería**: RabbitMQ, Apache Kafka
- **Contenedorización**: Docker, Docker Compose
- **Pruebas**: JUnit 5, Mockito, Cucumber (BDD)

## Arquitectura

El proyecto sigue una arquitectura por capas:

- **Controladores**: Gestión de endpoints REST
- **DTOs**: Objetos de transferencia de datos
- **Servicios**: Lógica de negocio
- **Repositorios**: Acceso a datos
- **Entidades**: Modelos de datos
- **Configuración**: Setup de componentes y servicios
- **Excepciones**: Manejo centralizado de errores
- **Utilidades**: Funciones auxiliares

Además, implementa patrones de diseño como:
- Repository
- DTO (Data Transfer Object)
- Dependency Injection
- Aspect-Oriented Programming

## Instalación y Ejecución

### Requisitos Previos

- Java 21 JDK
- Maven 3.8+
- Docker y Docker Compose (opcional, para contenedorización)

### Métodos de Ejecución

#### Con Docker Compose (Recomendado)

El proyecto incluye configuración completa para un entorno de desarrollo con:
- Aplicación Spring Boot
- RabbitMQ con interfaz de administración
- Kafka con su gestor UI
- Base de datos H2

Pasos:

1. **Compilar el proyecto**:
   ```bash
   mvn clean package
   ```

2. **Iniciar el entorno**:
   ```bash
   docker-compose up -d
   ```

3. **Verificar los logs**:
   ```bash
   docker-compose logs -f
   ```

4. **Detener el entorno**:
   ```bash
   docker-compose down
   ```

#### Ejecución Local (sin Docker)

1. **Compilar el proyecto**:
   ```bash
   mvn clean package
   ```

2. **Ejecutar la aplicación**:
   ```bash
   java -jar target/springboot-ships-backoffice-1.0-SNAPSHOT.jar
   ```

### Acceso a los Servicios

- **API REST**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **RabbitMQ Management**: http://localhost:15672
  - Usuario: `guest`
  - Contraseña: `guest`
- **Kafka UI**: http://localhost:9000

## Seguridad y Autenticación

El proyecto implementa un sistema de autenticación basado en JWT (JSON Web Token) con Spring Security:

- **Inicio de Sesión**: POST a `/api/login` con credenciales
- **Autenticación**: Token JWT incluido en header Authorization
- **Permisos**: Roles configurados en Spring Security
- **Expiración**: Tokens con tiempo limitado de validez

Credenciales para pruebas:
- **Usuario**: `test@test.com`
- **Contraseña**: `miContraseña123` (o en Base64: `bWlDb250cmFzZcOxYTEyMw==`)

## Documentación de la API

La API está completamente documentada con OpenAPI (Swagger):
- **URL de acceso**: http://localhost:8080/swagger-ui.html
- **Endpoints principales**:
  - `/api/ships` - Operaciones CRUD para naves
  - `/api/login` - Autenticación
  - `/api/kafka` - Operaciones con Kafka
  - `/api/rabbitmq` - Operaciones con RabbitMQ

## Integración con Sistemas de Mensajería

### RabbitMQ

El proyecto integra RabbitMQ para comunicación asíncrona:

- **Configuración**: Definida en `application.yml`
- **Controlador**: `RabbitMQController` expone endpoints para envío de mensajes
- **Consumidores**: Procesan mensajes recibidos

#### Ejecución de RabbitMQ Local

Si necesitas ejecutar RabbitMQ localmente sin Docker Compose:

```bash
docker run -d -p 9090:15672 -p 9091:5672 --name rabbitmq rabbitmq:3-management
```

### Kafka

El sistema también utiliza Apache Kafka para eventos relacionados con naves espaciales:

- **Configuración**: Definida en `application.yml`
- **Productores**: Envían eventos en operaciones CRUD (`KafkaSenderService`)
- **Consumidores**: Procesan mensajes asíncronos (`KafkaReceiverService`)
- **Controlador**: `KafkaController` permite enviar mensajes y verificar estados
- **Volumenes**: Configurados para persistencia de datos estan en el directorio interno de Docker

## Base de Datos

La aplicación utiliza H2 como base de datos embebida:

- **Migraciones**: Gestionadas por Flyway
- **Modelo de datos**: Entidades definidas en el paquete repository
- **Scripts**: Ubicados en `resources/migrations/`

El esquema incluye:
- Tabla de naves espaciales (`space_ships`)
- Tabla de usuarios (`users`)

## Pruebas

El proyecto incluye:

- **Pruebas unitarias**: Con JUnit 5 y Mockito
- **Pruebas de integración**: Con Cucumber (BDD)
- **Tests de rendimiento**: Para verificar tiempos de respuesta

### Ejecución de Pruebas

```bash
mvn test                 # Ejecutar pruebas unitarias
mvn verify               # Ejecutar todas las pruebas incluyendo integración
```

Las pruebas de integración utilizan escenarios Cucumber ubicados en `src/test/resources/integration/cucumber/`.
