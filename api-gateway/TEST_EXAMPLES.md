# 🧪 Ejemplos de Pruebas - API Gateway

## 📝 Guía de Testing con cURL

### 🔍 Health Checks

#### Verificar que el API Gateway está corriendo
```bash
curl http://localhost:8080/actuator/health
```

**Respuesta esperada:**
```json
{
  "status": "UP"
}
```

---

## 👥 USERS SERVICE

### Listar todos los usuarios
```bash
curl -X GET http://localhost:8080/api/users
```

### Obtener un usuario específico
```bash
curl -X GET http://localhost:8080/api/users/1
```

### Crear un nuevo usuario
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "María García",
    "email": "maria@example.com",
    "password": "password123"
  }'
```

### Actualizar un usuario
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "María García Actualizada",
    "email": "maria.updated@example.com"
  }'
```

### Eliminar un usuario
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

---

## 💳 ACCOUNTS SERVICE

### Listar todas las cuentas
```bash
curl -X GET http://localhost:8080/api/accounts
```

### **Caso de uso b: Anular cuenta de usuario**
```bash
# Anular cuenta (inhabilitar uso de la app)
curl -X PUT http://localhost:8080/api/accounts/1/anular
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "saldo": 100.50,
  "anulada": true,
  "fechaAlta": "2024-01-15T10:00:00"
}
```

### **Caso de uso b: Restaurar cuenta anulada**
```bash
# Restaurar cuenta
curl -X PUT http://localhost:8080/api/accounts/1/restaurar
```

### Verificar estado de una cuenta
```bash
curl -X GET http://localhost:8080/api/accounts/1/estado
```

**Respuesta esperada:**
```json
{
  "id": 1,
  "anulada": false
}
```

### Crear una nueva cuenta
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "saldo": 0.0,
    "fechaAlta": "2024-01-15T10:00:00"
  }'
```

### Actualizar saldo de cuenta
```bash
curl -X PUT http://localhost:8080/api/accounts/saldo/1 \
  -H "Content-Type: application/json" \
  -d '150.75'
```

### Vincular usuario a cuenta
```bash
curl -X PUT http://localhost:8080/api/accounts/1/link-user \
  -H "Content-Type: application/json" \
  -d '5'
```

### Desvincular usuario de cuenta
```bash
curl -X PUT http://localhost:8080/api/accounts/1/unlink-user \
  -H "Content-Type: application/json" \
  -d '5'
```

### Obtener usuarios de una cuenta
```bash
curl -X GET http://localhost:8080/api/accounts/1/users
```

---

## 🛴 MONOPATIN SERVICE

### Listar todos los monopatines
```bash
curl -X GET http://localhost:8080/api/monopatines
```

### **Caso de uso g: Buscar monopatines disponibles cercanos**
```bash
# Obtener solo monopatines disponibles
curl -X GET http://localhost:8080/api/monopatines/disponibles
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "latitud": -37.3215,
    "longitud": -59.1344,
    "estado": "DISPONIBLE",
    "kilometraje": 125.5
  },
  {
    "id": 2,
    "latitud": -37.3220,
    "longitud": -59.1350,
    "estado": "DISPONIBLE",
    "kilometraje": 89.2
  }
]
```

### **Caso de uso g: Buscar monopatines en una parada específica**
```bash
# Obtener monopatines de la parada 3
curl -X GET http://localhost:8080/api/monopatines/parada/3
```

### Obtener un monopatín específico
```bash
curl -X GET http://localhost:8080/api/monopatines/1
```

### Crear un nuevo monopatín
```bash
curl -X POST http://localhost:8080/api/monopatines \
  -H "Content-Type: application/json" \
  -d '{
    "latitud": -37.3215,
    "longitud": -59.1344,
    "estado": "DISPONIBLE"
  }'
```

### Actualizar monopatín
```bash
curl -X PUT http://localhost:8080/api/monopatines/1 \
  -H "Content-Type: application/json" \
  -d '{
    "latitud": -37.3220,
    "longitud": -59.1350
  }'
```

### Registrar monopatín en mantenimiento
```bash
curl -X PATCH http://localhost:8080/api/monopatines/1/mantenimiento
```

### Activar monopatín
```bash
curl -X PATCH http://localhost:8080/api/monopatines/1/activar
```

### Desactivar monopatín
```bash
curl -X PATCH http://localhost:8080/api/monopatines/1/desactivar
```

### Actualizar parada del monopatín
```bash
curl -X PUT http://localhost:8080/api/monopatines/parada/1 \
  -H "Content-Type: application/json" \
  -d '3'
```

### Eliminar monopatín
```bash
curl -X DELETE http://localhost:8080/api/monopatines/1
```

---

## 📍 PARADAS SERVICE

### Listar todas las paradas
```bash
curl -X GET http://localhost:8080/api/paradas
```

### Obtener una parada específica
```bash
curl -X GET http://localhost:8080/api/paradas/1
```

### Registrar nueva parada
```bash
curl -X POST http://localhost:8080/api/paradas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Plaza Moreno",
    "latitud": -37.3215,
    "longitud": -59.1344
  }'
```

### Actualizar parada
```bash
curl -X PUT http://localhost:8080/api/paradas/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Plaza Moreno - Actualizada",
    "latitud": -37.3220,
    "longitud": -59.1350
  }'
```

### Ubicar monopatín en parada
```bash
curl -X PUT http://localhost:8080/api/paradas/1/ubicar-monopatin \
  -H "Content-Type: application/json" \
  -d '5'
```

### Obtener monopatines de una parada
```bash
curl -X GET http://localhost:8080/api/paradas/1/monopatines
```

### Eliminar parada
```bash
curl -X DELETE http://localhost:8080/api/paradas/1
```

---

## 🔧 ADMIN SERVICE

### Listar todos los monopatines (admin)
```bash
curl -X GET http://localhost:8080/api/admin/monopatines
```

### Crear monopatín (admin)
```bash
curl -X POST http://localhost:8080/api/admin/monopatines \
  -H "Content-Type: application/json" \
  -d '{
    "latitud": -37.3215,
    "longitud": -59.1344
  }'
```

### Actualizar monopatín (admin)
```bash
curl -X PUT http://localhost:8080/api/admin/monopatines/1 \
  -H "Content-Type: application/json" \
  -d '{
    "latitud": -37.3220,
    "longitud": -59.1350
  }'
```

### Eliminar monopatín (admin)
```bash
curl -X DELETE http://localhost:8080/api/admin/monopatines/1
```

---

## 📊 REPORTE SERVICE

### **Caso de uso a: Reporte de uso por kilómetros**
```bash
# Obtener reporte de monopatines usados (para mantenimiento)
curl -X GET http://localhost:8080/api/reportes/monopatin/usado
```

**Respuesta esperada:**
```json
[
  {
    "id": 1,
    "kilometraje": 245.8,
    "tiempoUso": "150h 30m",
    "requiereMantenimiento": true
  },
  {
    "id": 2,
    "kilometraje": 89.2,
    "tiempoUso": "45h 15m",
    "requiereMantenimiento": false
  }
]
```

### **Caso de uso c: Monopatines con más de X viajes** (Pendiente)
```bash
# Ejemplo de cómo sería la petición
curl -X GET "http://localhost:8080/api/reportes/monopatines/viajes?minViajes=50&anio=2024"
```

### **Caso de uso d: Total facturado en rango de meses** (Pendiente)
```bash
# Ejemplo de cómo sería la petición
curl -X GET "http://localhost:8080/api/reportes/facturacion?mesInicio=1&mesFin=6&anio=2024"
```

### **Caso de uso e: Usuarios top** (Pendiente)
```bash
# Ejemplo de cómo sería la petición
curl -X GET "http://localhost:8080/api/reportes/usuarios/top?desde=2024-01-01&hasta=2024-12-31&tipoUsuario=PREMIUM"
```

### **Caso de uso h: Uso por usuario en período** (Pendiente)
```bash
# Ejemplo de cómo sería la petición
curl -X GET "http://localhost:8080/api/reportes/usuarios/123/uso?desde=2024-01-01&hasta=2024-12-31&incluirRelacionados=true"
```

---

## 🧪 Probar Circuit Breaker

### Paso 1: Detener un servicio
```bash
# Detén el servicio de usuarios (puerto 8081)
# En Windows: Ctrl+C en la terminal del servicio
# En Linux/Mac: kill -9 <PID>
```

### Paso 2: Hacer peticiones al servicio caído
```bash
# Primera petición
curl -X GET http://localhost:8080/api/users

# Segunda petición
curl -X GET http://localhost:8080/api/users

# ... repetir hasta que se abra el Circuit Breaker
```

### Paso 3: Verificar respuesta de fallback
**Después de 5 fallos, recibirás:**
```json
{
  "error": "Service Temporarily Unavailable",
  "service": "Users Service",
  "message": "El servicio Users Service no está disponible...",
  "timestamp": "2024-01-15T10:30:00",
  "status": 503
}
```

### Paso 4: Ver estado del Circuit Breaker
```bash
curl http://localhost:8080/actuator/circuitbreakers
```

**Respuesta esperada:**
```json
{
  "circuitBreakers": {
    "usersCircuitBreaker": {
      "state": "OPEN",
      "failureRate": "100.0%",
      "numberOfFailedCalls": 5
    }
  }
}
```

### Paso 5: Reiniciar el servicio y verificar recuperación
```bash
# Reinicia el servicio users-service
# Espera 10 segundos (waitDurationInOpenState)

# El Circuit Breaker pasará a HALF_OPEN y probará el servicio
curl -X GET http://localhost:8080/api/users

# Si tiene éxito, volverá a CLOSED
curl http://localhost:8080/actuator/circuitbreakers
```

---

## 📊 Monitoreo en Tiempo Real

### Ver todos los Circuit Breakers
```bash
curl http://localhost:8080/actuator/circuitbreakers | json_pp
```

### Ver eventos de Circuit Breakers
```bash
curl http://localhost:8080/actuator/circuitbreakerevents | json_pp
```

### Ver rutas configuradas
```bash
curl http://localhost:8080/actuator/gateway/routes | json_pp
```

---

## 🎯 Flujo de Trabajo Completo de Prueba

### Escenario: Usuario renta un monopatín

```bash
# 1. Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos López",
    "email": "carlos@example.com",
    "password": "pass123"
  }'
# Respuesta: {"id": 10, ...}

# 2. Crear cuenta para el usuario
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "saldo": 500.0,
    "fechaAlta": "2024-01-15T10:00:00"
  }'
# Respuesta: {"id": 5, ...}

# 3. Vincular usuario a cuenta
curl -X PUT http://localhost:8080/api/accounts/5/link-user \
  -H "Content-Type: application/json" \
  -d '10'

# 4. Buscar monopatines disponibles cerca
curl -X GET http://localhost:8080/api/monopatines/disponibles

# 5. Seleccionar monopatín y verificar que esté en la parada
curl -X GET http://localhost:8080/api/paradas/3/monopatines

# 6. Usuario usa el monopatín...
# (aquí irían las operaciones de viaje)

# 7. Verificar saldo después del viaje
curl -X GET http://localhost:8080/api/accounts/5
```

---

## 💡 Tips de Testing

### Usar variables en bash
```bash
# Definir variables
export API_URL="http://localhost:8080"
export USER_ID=1
export ACCOUNT_ID=5

# Usar en las peticiones
curl -X GET $API_URL/api/users/$USER_ID
curl -X GET $API_URL/api/accounts/$ACCOUNT_ID
```

### Formatear respuestas JSON
```bash
# Con jq (instalar: apt-get install jq o brew install jq)
curl http://localhost:8080/api/users | jq

# Con json_pp (viene con Perl)
curl http://localhost:8080/api/users | json_pp
```

### Guardar respuestas en archivos
```bash
curl http://localhost:8080/api/users > users.json
curl http://localhost:8080/api/monopatines/disponibles > monopatines.json
```

### Medir tiempos de respuesta
```bash
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/users
```

Archivo `curl-format.txt`:
```
time_total:  %{time_total}s
time_connect: %{time_connect}s
```

---

## ✅ Checklist de Pruebas

- [ ] API Gateway está corriendo (puerto 8080)
- [ ] Todos los servicios están corriendo (8081-8086)
- [ ] Health checks responden correctamente
- [ ] CORS funciona desde el navegador
- [ ] Todos los endpoints de Users funcionan
- [ ] Todos los endpoints de Accounts funcionan
- [ ] Se puede anular y restaurar cuentas
- [ ] Todos los endpoints de Monopatines funcionan
- [ ] Se pueden buscar monopatines disponibles
- [ ] Todos los endpoints de Paradas funcionan
- [ ] Todos los endpoints de Admin funcionan
- [ ] Endpoint de reportes funciona
- [ ] Circuit Breaker se abre tras múltiples fallos
- [ ] Fallback handlers responden correctamente
- [ ] Circuit Breaker se cierra cuando el servicio vuelve