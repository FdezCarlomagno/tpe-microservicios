package tpe.microservicios.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/**
 * Filtro global que registra información de todas las peticiones
 * que pasan por el API Gateway
 */
@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Instant startTime = Instant.now();

        // Log de la petición entrante
        log.info("═══════════════════════════════════════════════════");
        log.info("📥 REQUEST  → {} {}",
                request.getMethod(),
                request.getURI());
        log.info("🌐 Remote Address: {}",
                request.getRemoteAddress());

        // Continuar con la cadena de filtros y loguear la respuesta
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            Duration duration = Duration.between(startTime, Instant.now());

            log.info("📤 RESPONSE ← {} {} - Status: {} - Time: {}ms",
                    request.getMethod(),
                    request.getURI(),
                    response.getStatusCode(),
                    duration.toMillis());
            log.info("═══════════════════════════════════════════════════");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Ejecutar antes que otros filtros
    }
}