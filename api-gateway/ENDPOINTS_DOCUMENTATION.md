# 📚 Documentación de Endpoints - API Gateway

## 🌐 Base URL
```
http://localhost:8080
```

---

## 👥 USERS SERVICE (Puerto 8081)

### Endpoints Disponibles:

#### 1. Obtener todos los usuarios
```http
GET /api/users
```

#### 2. Obtener usuario por ID
```http
GET /api/users/{id}
```

#### 3. Crear usuario
```http
POST /api/users
Content-Type: application/json

{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

#### 4. Actualizar usuario
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "name": "string",
  "email": "string"
}
```

#### 5. Eliminar usuario
```http
DELETE /api/users/{id}
```

#### 6. Health Check
```http
GET /api/users/health
```

---

## 💳 ACCOUNTS SERVICE (Puerto 8082)

### Endpoints Disponibles:

#### 1. Obtener todas las cuentas
```http
GET /api/accounts
```

#### 2. Obtener cuenta por ID
```http
GET /api/accounts/{id}
```

#### 3. **b) Anular cuenta de usuario** ✅
```http
PUT /api/accounts/{id}/anular
```
**Descripción:** Inhabilita temporalmente el uso de la aplicación para un usuario

#### 4. **b) Restaurar cuenta anulada** ✅
```http
PUT /api/accounts/{id}/restaurar
```

#### 5. Obtener usuarios de una cuenta
```http
GET /api/accounts/{id}/users
```

#### 6. Crear cuenta de usuario
```http
POST /api/accounts
Content-Type: application/json

{
  "saldo": 0.0,
  "fechaAlta": "2024-01-01T00:00:00"
}
```

#### 7. Actualizar saldo
```http
PUT /api/accounts/saldo/{id}
Content-Type: application/json

100.50
```

#### 8. Vincular usuario a cuenta
```http
PUT /api/accounts/{id}/link-user
Content-Type: application/json

123
```

#### 9. Desvincular usuario de cuenta
```http
PUT /api/accounts/{id}/unlink-user
Content-Type: application/json

123
```

#### 10. Eliminar cuenta
```http
DELETE /api/accounts/{id}
```

#### 11. Verificar estado de cuenta
```http
GET /api/accounts/{id}/estado
```

#### 12. Health Check
```http
GET /api/accounts/health
```

---

## 🛴 MONOPATIN SERVICE (Puerto 8083)

### Endpoints Disponibles:

#### 1. Obtener todos los monopatines
```http
GET /api/monopatines
```

#### 2. Obtener monopatín por ID
```http
GET /api/monopatines/{id}
```

#### 3. **g) Obtener monopatines disponibles** ✅
```http
GET /api/monopatines/disponibles
```
**Descripción:** Lista monopatines cercanos disponibles para usar

#### 4. **g) Obtener monopatines por parada** ✅
```http
GET /api/monopatines/parada/{idParada}
```
**Descripción:** Busca monopatines en una ubicación específica

#### 5. Crear monopatín
```http
POST /api/monopatines
Content-Type: application/json

{
  "latitud": 0.0,
  "longitud": 0.0,
  "estado": "DISPONIBLE"
}
```

#### 6. Actualizar monopatín
```http
PUT /api/monopatines/{id}
Content-Type: application/json

{
  "latitud": 0.0,
  "longitud": 0.0
}
```

#### 7. Registrar en mantenimiento
```http
PATCH /api/monopatines/{id}/mantenimiento
```

#### 8. Activar monopatín
```http
PATCH /api/monopatines/{id}/activar
```

#### 9. Desactivar monopatín
```http
PATCH /api/monopatines/{id}/desactivar
```

#### 10. Eliminar monopatín
```http
DELETE /api/monopatines/{id}
```

#### 11. Actualizar parada del monopatín
```http
PUT /api/monopatines/parada/{id}
Content-Type: application/json

456
```

---

## 📍 PARADAS SERVICE (Puerto 8084)

### Endpoints Disponibles:

#### 1. Obtener todas las paradas
```http
GET /api/paradas
```

#### 2. Obtener parada por ID
```http
GET /api/paradas/{id}
```

#### 3. Registrar nueva parada
```http
POST /api/paradas
Content-Type: application/json

{
  "nombre": "string",
  "latitud": 0.0,
  "longitud": 0.0
}
```

#### 4. Actualizar parada
```http
PUT /api/paradas/{id}
Content-Type: application/json

{
  "nombre": "string",
  "latitud": 0.0,
  "longitud": 0.0
}
```

#### 5. Eliminar parada
```http
DELETE /api/paradas/{id}
```

#### 6. Ubicar monopatín en parada
```http
PUT /api/paradas/{id}/ubicar-monopatin
Content-Type: application/json

789
```

#### 7. Obtener monopatines de una parada
```http
GET /api/paradas/{id}/monopatines
```

---

## 🔧 ADMIN SERVICE (Puerto 8085)

### Endpoints Disponibles:

#### 1. Listar todos los monopatines (admin)
```http
GET /api/admin/monopatines
```

#### 2. Crear monopatín (admin)
```http
POST /api/admin/monopatines
Content-Type: application/json

{
  "latitud": 0.0,
  "longitud": 0.0
}
```

#### 3. Actualizar monopatín (admin)
```http
PUT /api/admin/monopatines/{id}
Content-Type: application/json

{
  "latitud": 0.0,
  "longitud": 0.0
}
```

#### 4. Eliminar monopatín (admin)
```http
DELETE /api/admin/monopatines/{id}
```

---

## 📊 REPORTE SERVICE (Puerto 8086)

### Endpoints Disponibles:

#### 1. **a) Reporte de monopatines usados por kilómetros** ✅
```http
GET /api/reportes/monopatin/usado
```
**Descripción:** Genera reporte de uso por kilómetros para determinar necesidad de mantenimiento

#### 2. **c) Monopatines con más de X viajes en un año** ⚠️ (Pendiente de implementar)
```http
GET /api/reportes/monopatines/viajes?minViajes={cantidad}&anio={año}
```

#### 3. **d) Total facturado en rango de meses** ⚠️ (Pendiente de implementar)
```http
GET /api/reportes/facturacion?mesInicio={mes}&mesFin={mes}&anio={año}
```

#### 4. **e) Usuarios que más usan monopatines** ⚠️ (Pendiente de implementar)
```http
GET /api/reportes/usuarios/top?desde={fecha}&hasta={fecha}&tipoUsuario={tipo}
```

#### 5. **f) Ajuste de precios** ⚠️ (Pendiente de implementar)
```http
POST /api/reportes/precios/ajuste
Content-Type: application/json

{
  "precioNormal": 50.0,
  "precioPausa": 10.0,
  "fechaVigencia": "2024-06-01"
}
```

#### 6. **h) Uso de monopatines por usuario en período** ⚠️ (Pendiente de implementar)
```http
GET /api/reportes/usuarios/{id}/uso?desde={fecha}&hasta={fecha}&incluirRelacionados={boolean}
```

---

## 🔄 Circuit Breaker & Health

### Verificar estado del API Gateway
```http
GET /actuator/health
```

### Verificar estado de Circuit Breakers
```http
GET /actuator/circuitbreakers
```

### Eventos de Circuit Breakers
```http
GET /actuator/circuitbreakerevents
```

---

## 🚨 Respuestas de Fallback

Cuando un servicio no está disponible, el API Gateway responde con:

```json
{
  "error": "Service Temporarily Unavailable",
  "service": "Nombre del Servicio",
  "message": "El servicio X no está disponible en este momento. Por favor, intente nuevamente en unos momentos.",
  "timestamp": "2024-01-15T10:30:00",
  "status": 503
}
```

---

## 📝 Notas Importantes

### ✅ Endpoints Implementados según Requerimientos:
- **(b)** Anular/Restaurar cuentas de usuarios
- **(a)** Reporte de uso por kilómetros
- **(g)** Búsqueda de monopatines cercanos

### ⚠️ Endpoints Pendientes de Implementación:
- **(c)** Monopatines con más de X viajes
- **(d)** Total facturado en rango de meses
- **(e)** Usuarios que más utilizan monopatines
- **(f)** Ajuste de precios
- **(h)** Uso de monopatines por usuario en período

### 🔒 CORS Habilitado
- Todos los orígenes permitidos (*)
- Todos los métodos HTTP permitidos
- Credenciales habilitadas

### 🛡️ Circuit Breaker Configurado
- Umbral de fallo: 50%
- Ventana deslizante: 10 llamadas
- Tiempo en estado abierto: 10 segundos
- Llamadas en semi-abierto: 3