# API de Tarifas - Viajes Service

## Descripción
Las tarifas del servicio de viajes ahora se almacenan en la base de datos, permitiendo que un administrador las actualice dinámicamente sin necesidad de reiniciar el servicio.

## Cambios Realizados

### 1. **Entidades de Base de Datos**
- **Tarifa.java**: Entidad JPA que almacena las tarifas en la tabla `tarifas`
  - `id`: Identificador único
  - `tipo`: Tipo de tarifa (NORMAL, PAUSA_EXCEDIDA, REANUDADA, PAUSA_MAX_MINUTOS)
  - `valor`: Valor numérico de la tarifa
  - `pausaMaxMinutos`: Configuración de pausa máxima (solo para tipo PAUSA_MAX_MINUTOS)

### 2. **Repositorio**
- **TarifaRepository.java**: Interfaz JPA para acceder a las tarifas en BD

### 3. **Servicio**
- **TarifaService.java**: 
  - Obtiene tarifas desde la BD en lugar de `application.properties`
  - Método `calcularCosto()`: Calcula el costo del viaje usando tarifas de BD
  - Método `obtenerTarifa()`: Obtiene una tarifa específica
  - Método `actualizarTarifa()`: Actualiza el valor de una tarifa
  - Método `crearTarifa()`: Crea una nueva tarifa

### 4. **Controlador REST**
- **TarifaController.java**: Expone endpoints para gestionar tarifas

#### Endpoints Disponibles

**GET /api/tarifas**
- Obtiene todas las tarifas
- Response: Lista de TarifaResponseDTO

**GET /api/tarifas/{tipo}**
- Obtiene una tarifa específica por tipo
- Parámetros:
  - `tipo`: Tipo de tarifa (NORMAL, PAUSA_EXCEDIDA, etc.)
- Response: TarifaResponseDTO

**POST /api/tarifas**
- Crea una nueva tarifa (requiere permisos de administrador)
- Request Body:
  ```json
  {
    "tipo": "NORMAL",
    "valor": 1.0,
    "pausaMaxMinutos": null
  }
  ```
- Response: TarifaResponseDTO

**PUT /api/tarifas/{tipo}**
- Actualiza una tarifa existente (requiere permisos de administrador)
- Parámetros:
  - `tipo`: Tipo de tarifa a actualizar
- Request Body:
  ```json
  {
    "tipo": "NORMAL",
    "valor": 1.5,
    "pausaMaxMinutos": null
  }
  ```
- Response: TarifaResponseDTO

**DELETE /api/tarifas/{id}**
- Elimina una tarifa (requiere permisos de administrador)
- Parámetros:
  - `id`: Identificador de la tarifa

### 5. **Inicializador**
- **TarifaInitializer.java**: Componente que carga tarifas por defecto en la base de datos la primera vez que se ejecuta la aplicación

### 6. **Cambios en Configuration**
- Eliminadas las propiedades de configuración de tarifas de `application.properties`

## DTOs

### TarifaRequestDTO
```java
record TarifaRequestDTO(
    String tipo,           // Tipo de tarifa
    Float valor,           // Valor de la tarifa
    Integer pausaMaxMinutos // Minutos máximo de pausa (opcional)
)
```

### TarifaResponseDTO
```java
record TarifaResponseDTO(
    Long id,                // ID de la tarifa
    String tipo,            // Tipo de tarifa
    Float valor,            // Valor de la tarifa
    Integer pausaMaxMinutos // Minutos máximo de pausa
)
```

## Ejemplo de Uso desde Otro Microservicio

```java
// Usando Feign Client
@FeignClient(name = "viajes-service")
public interface TarifasClient {
    
    @GetMapping("/api/tarifas")
    List<TarifaResponseDTO> obtenerTodasLasTarifas();
    
    @GetMapping("/api/tarifas/{tipo}")
    TarifaResponseDTO obtenerTarifa(@PathVariable String tipo);
    
    @PutMapping("/api/tarifas/{tipo}")
    TarifaResponseDTO actualizarTarifa(
        @PathVariable String tipo,
        @RequestBody TarifaRequestDTO request
    );
}
```

## Tarifas Iniciales

Cuando la aplicación inicia por primera vez, se cargan las siguientes tarifas:

| Tipo | Valor | Descripción |
|------|-------|-------------|
| NORMAL | 1.0 | Tarifa base por minuto de viaje |
| PAUSA_EXCEDIDA | 2.0 | Recargo por minuto de pausa excedida |
| REANUDADA | 1.5 | Tarifa para viajes reanudados |
| PAUSA_MAX_MINUTOS | 0f | Configuración: máximo de minutos de pausa permitidos (15) |

## Notas de Seguridad

- Los endpoints de modificación (POST, PUT, DELETE) deben estar protegidos con autenticación y autorización (roles de administrador)
- Se recomienda implementar Spring Security con control de acceso basado en roles
