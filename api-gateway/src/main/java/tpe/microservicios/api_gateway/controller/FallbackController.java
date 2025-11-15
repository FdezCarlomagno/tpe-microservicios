package tpe.microservicios.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador que maneja los fallbacks cuando los servicios no están disponibles
 * debido a que el Circuit Breaker está abierto.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Fallback para el servicio de usuarios
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> usersFallback() {
        return buildFallbackResponse("Users Service");
    }

    /**
     * Fallback para el servicio de cuentas
     */
    @GetMapping("/accounts")
    public ResponseEntity<Map<String, Object>> accountsFallback() {
        return buildFallbackResponse("Accounts Service");
    }

    /**
     * Fallback para el servicio de monopatines
     */
    @GetMapping("/monopatines")
    public ResponseEntity<Map<String, Object>> monopatinesFallback() {
        return buildFallbackResponse("Monopatines Service");
    }

    /**
     * Fallback para el servicio de paradas
     */
    @GetMapping("/paradas")
    public ResponseEntity<Map<String, Object>> paradasFallback() {
        return buildFallbackResponse("Paradas Service");
    }

    /**
     * Fallback para el servicio de administración
     */
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> adminFallback() {
        return buildFallbackResponse("Admin Service");
    }

    /**
     * Fallback para el servicio de viajes
     */
    @GetMapping("/viajes")
    public ResponseEntity<Map<String, Object>> viajesFallback() {
        return buildFallbackResponse("Viajes Service");
    }

    /**
     * Fallback para el servicio de reportes
     */
    @GetMapping("/reportes")
    public ResponseEntity<Map<String, Object>> reportesFallback() {
        return buildFallbackResponse("Reportes Service");
    }

    /**
     * Construye la respuesta estándar de fallback
     */
    private ResponseEntity<Map<String, Object>> buildFallbackResponse(String serviceName) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Service Temporarily Unavailable");
        response.put("service", serviceName);
        response.put("message",
                "El servicio " + serviceName + " no está disponible en este momento. " +
                        "Por favor, intente nuevamente en unos momentos.");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }
}