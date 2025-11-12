# API Gateway - Sistema de Monopatines

API Gateway centralizado para el sistema de gestión de monopatines. Proporciona un único punto de entrada para todos los microservicios, incluyendo el nuevo **servicio de viajes**.

---

## 📋 Tabla de Contenidos

* [Características](#características)
* [Arquitectura](#arquitectura)
* [Requisitos](#requisitos)
* [Instalación](#instalación)
* [Configuración](#configuración)
* [Uso](#uso)
* [Testing](#testing)
* [Troubleshooting](#troubleshooting)
* [Monitoreo](#monitoreo)
* [Logs](#logs)
* [Soporte](#soporte)

---

## ✨ Características

* ✅ **Enrutamiento Dinámico**: Redirige peticiones a los microservicios correspondientes.
* ✅ **CORS Habilitado**: Permite peticiones desde cualquier origen.
* ✅ **Circuit Breaker**: Implementado con Resilience4j para mayor resiliencia.
* ✅ **Fallback Handlers**: Respuestas elegantes cuando los servicios no están disponibles.
* ✅ **Logging**: Registro detallado de todas las peticiones.
* ✅ **Health Checks**: Monitoreo del estado de todos los servicios.
* ✅ **Timeout Configuration**: Gestión de tiempos de espera.

---

## 🏗️ Arquitectura

```
┌─────────────┐
│   Cliente   │
└──────┬──────┘
       │
       ▼
┌──────────────────────────────┐
│      API Gateway (8080)      │
│  ┌────────────────────────┐  │
│  │  Circuit Breaker       │  │
│  │  CORS Filter           │  │
│  │  Logging Filter        │  │
│  └────────────────────────┘  │
└──────────────┬───────────────┘
               │
    ┌──────────┼──────────┬──────────┬──────────┬──────────┬──────────┐
    ▼          ▼          ▼          ▼          ▼          ▼          ▼
┌────────┐ ┌─────────┐ ┌──────────┐ ┌────────┐ ┌───────┐ ┌─────────┐ ┌────────┐
│ Users  │ │Accounts │ │Monopatin │ │Paradas │ │ Admin │ │Reportes │ │ Viajes │
│  8081  │ │  8082   │ │   8083   │ │  8084  │ │ 8085  │ │  8086   │ │  8087  │
└────────┘ └─────────┘ └──────────┘ └────────┘ └───────┘ └─────────┘ └────────┘
```

---

## 📦 Requisitos

* **Java**: 17 o superior
* **Maven**: 3.8+
* **Spring Boot**: 3.2.0
* **Spring Cloud**: 2023.0.0

### Servicios que deben estar corriendo:

| Servicio           | Puerto   |
| ------------------ | -------- |
| users-service      | 8081     |
| accounts-service   | 8082     |
| monopatin-service  | 8083     |
| paradas-service    | 8084     |
| admin-service      | 8085     |
| reporte-service    | 8086     |
| **viajes-service** | **8087** |

---

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
git clone <tu-repositorio>
cd api-gateway
```

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar el API Gateway

```bash
mvn spring-boot:run
```

O con el JAR generado:

```bash
java -jar target/api-gateway-1.0.0.jar
```

---

## ⚙️ Configuración

### Modificar puertos de los servicios

Si tus servicios corren en puertos diferentes, edita `application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: users-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/users/**
        - id: accounts-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/accounts/**
        - id: monopatin-service
          uri: http://localhost:8083
          predicates:
            - Path=/api/monopatines/**
        - id: paradas-service
          uri: http://localhost:8084
          predicates:
            - Path=/api/paradas/**
        - id: admin-service
          uri: http://localhost:8085
          predicates:
            - Path=/api/admin/**
        - id: reporte-service
          uri: http://localhost:8086
          predicates:
            - Path=/api/reportes/**
        - id: viajes-service
          uri: http://localhost:8087
          predicates:
            - Path=/api/viajes/**
```

### Configurar Circuit Breaker

```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 10
        minimumNumberOfCalls: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10000
```

### Configurar CORS

```java
corsConfig.setAllowedOriginPatterns(Arrays.asList(
    "http://localhost:3000",
    "https://tu-dominio.com"
));
```

---

## 🎯 Uso

Todas las peticiones deben hacerse a través del API Gateway (puerto **8080**).

### Ejemplos de uso con cURL

#### Obtener todos los usuarios

```bash
curl -X GET http://localhost:8080/api/users
```

#### Crear un usuario

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Juan Pérez", "email": "juan@example.com", "password": "123456"}'
```

#### Crear un viaje

```bash
curl -X POST http://localhost:8080/api/viajes \
  -H "Content-Type: application/json" \
  -d '{"idMonopatin": 1, "idAccount": 2, "idParadaInicio": 3}'
```

#### Obtener un viaje por ID

```bash
curl -X GET http://localhost:8080/api/viajes/5
```

#### Pausar un viaje

```bash
curl -X PUT http://localhost:8080/api/viajes/5/pausar \
  -H "Content-Type: application/json" \
  -d '{"idAccount": 2}'
```

#### Reanudar un viaje

```bash
curl -X PUT http://localhost:8080/api/viajes/5/reanudar \
  -H "Content-Type: application/json" \
  -d '{"idAccount": 2}'
```

#### Finalizar un viaje

```bash
curl -X PUT http://localhost:8080/api/viajes/5/finalizar \
  -H "Content-Type: application/json" \
  -d '{"idParadaFin": 8, "idAccount": 2}'
```

#### Eliminar un viaje

```bash
curl -X DELETE http://localhost:8080/api/viajes/5
```

---

## 🧪 Testing

1. Importa la colección Postman `ENDPOINTS_DOCUMENTATION.md`.
2. Configura `BASE_URL = http://localhost:8080`.
3. Ejecuta peticiones de prueba a los servicios, incluyendo `/api/viajes`.

---

## 🐛 Troubleshooting

### Problema: "Connection refused"

**Causa:** Uno o más microservicios no están corriendo.
**Solución:**

```bash
netstat -an | grep LISTEN | grep "808[1-7]"
```

Debe mostrar:

```
8081 (users)
8082 (accounts)
8083 (monopatin)
8084 (paradas)
8085 (admin)
8086 (reportes)
8087 (viajes)
```

---

## 📊 Monitoreo

Endpoints de Actuator disponibles:

* `/actuator/health`
* `/actuator/info`
* `/actuator/gateway/routes`
* `/actuator/circuitbreakers`
* `/actuator/circuitbreakerevents`

Logs en tiempo real:

```bash
tail -f logs/api-gateway.log
```

---

## 📝 Logs

El API Gateway registra:

* Todas las peticiones entrantes y salientes
* Tiempos de respuesta
* Circuit Breaker events
* Errores y excepciones

Ejemplo:

```
📥 REQUEST  → GET http://localhost:8080/api/viajes/5
📤 RESPONSE ← GET http://localhost:8080/api/viajes/5 - Status: 200 OK - Time: 180ms
```

---

## 🎉 ¡Listo!

El **API Gateway** está configurado y funcionando.
Todos los microservicios —incluido **Viajes Service (8087)**— son accesibles a través del **puerto 8080**.

---

## 📞 Soporte

Si tenés problemas:

1. Revisá los logs del API Gateway.
2. Verificá que todos los servicios estén corriendo.
3. Consultá esta guía.
4. Ajustá la configuración de Circuit Breaker si es necesario.
