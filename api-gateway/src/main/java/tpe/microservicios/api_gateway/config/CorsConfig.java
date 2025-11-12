package tpe.microservicios.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de CORS para el API Gateway
 * Permite peticiones desde cualquier origen con todos los métodos HTTP
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        org.springframework.web.cors.CorsConfiguration corsConfig =
                new org.springframework.web.cors.CorsConfiguration();

        // Permitir todos los orígenes (en producción, especificar orígenes concretos)
        corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));

        // Permitir todos los métodos HTTP
        corsConfig.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Permitir todos los headers
        corsConfig.setAllowedHeaders(Arrays.asList("*"));

        // Permitir credenciales
        corsConfig.setAllowCredentials(true);

        // Headers expuestos al cliente
        corsConfig.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"
        ));

        // Tiempo de caché de la configuración CORS
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}