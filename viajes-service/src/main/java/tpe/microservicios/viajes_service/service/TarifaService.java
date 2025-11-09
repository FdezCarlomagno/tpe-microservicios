package tpe.microservicios.viajes_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class TarifaService {

    @Value("${tarifa.normal}")
    private float tarifaNormal;

    @Value("${tarifa.pausa.excedida}")
    private float tarifaPausaExcedida;

    @Value("${tarifa.reanudada}")
    private float tarifaReanudada;

    @Value("${pausa.max.minutos}")
    private int pausaMaxMinutos;

    public float calcularCosto(LocalDateTime inicio, LocalDateTime fin, long minutosPausaTotal) {
        long duracionTotal = Duration.between(inicio, fin).toMinutes();
        long minutosExcedidos = Math.max(0, minutosPausaTotal - pausaMaxMinutos);

        // Cobro base por minuto + recargo por exceder pausa
        return (duracionTotal * tarifaNormal)
                + (minutosExcedidos * tarifaPausaExcedida);
    }
}
