package tpe.microservicios.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * API Gateway para el sistema de gestión de monopatines
 *
 * Este gateway actúa como punto de entrada único para todos los microservicios:
 * - users-service (8081)
 * - accounts-service (8082)
 * - monopatin-service (8083)
 * - paradas-service (8084)
 * - admin-service (8085)
 * - reporte-service (8086)
 *
 * Características implementadas:
 * - Enrutamiento dinámico
 * - Circuit Breaker con Resilience4j
 * - CORS habilitado
 * - Fallback handlers para cada servicio
 */
@SpringBootApplication
@EnableFeignClients
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("==============================================");
        System.out.println("🚀 API Gateway iniciado en puerto 8080");
        System.out.println("==============================================");
        System.out.println("📍 Servicios disponibles:");
        System.out.println("   → Users:      http://localhost:8080/api/users");
        System.out.println("   → Accounts:   http://localhost:8080/api/accounts");
        System.out.println("   → Monopatines: http://localhost:8080/api/monopatines");
        System.out.println("   → Paradas:    http://localhost:8080/api/paradas");
        System.out.println("   → Viajes:    http://localhost:8080/api/viajes");
        System.out.println("   → Admin:      http://localhost:8080/api/admin");
        System.out.println("   → Reportes:   http://localhost:8080/api/reportes");
        System.out.println("==============================================");
        System.out.println("📊 Actuator:     http://localhost:8080/actuator/health");
        System.out.println("🔄 Circuit Breakers: http://localhost:8080/actuator/circuitbreakers");
        System.out.println("==============================================");
    }
}