package tpe.microservicios.viajes_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tpe.microservicios.viajes_service.domains.Tarifa;
import tpe.microservicios.viajes_service.exceptions.BadRequestException;
import tpe.microservicios.viajes_service.exceptions.NotFoundException;
import tpe.microservicios.viajes_service.repository.TarifaRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private final TarifaRepository tarifaRepository;

    private static final String TARIFA_NORMAL = "NORMAL";
    private static final String TARIFA_PAUSA_EXCEDIDA = "PAUSA_EXCEDIDA";
    private static final String PAUSA_MAX_MINUTOS = "PAUSA_MAX_MINUTOS";

    public float calcularCosto(LocalDateTime inicio, LocalDateTime fin, long minutosPausaTotal) {
        long duracionTotal = Duration.between(inicio, fin).toMinutes();
        
        Tarifa tarifaNormalObj = tarifaRepository.findByTipo(TARIFA_NORMAL)
                .orElseThrow(() -> new NotFoundException("Tarifa NORMAL no configurada"));
        
        Tarifa tarifaPausaExcedidaObj = tarifaRepository.findByTipo(TARIFA_PAUSA_EXCEDIDA)
                .orElseThrow(() -> new NotFoundException("Tarifa PAUSA_EXCEDIDA no configurada"));
        
        Tarifa pausaMaxMinutosObj = tarifaRepository.findByTipo(PAUSA_MAX_MINUTOS)
                .orElseThrow(() -> new NotFoundException("Configuración PAUSA_MAX_MINUTOS no existe"));

        float tarifaNormal = tarifaNormalObj.getValor();
        float tarifaPausaExcedida = tarifaPausaExcedidaObj.getValor();
        int pausaMaxMinutos = pausaMaxMinutosObj.getPausaMaxMinutos();
        
        long minutosExcedidos = Math.max(0, minutosPausaTotal - pausaMaxMinutos);

        // Cobro base por minuto + recargo por exceder pausa
        return (duracionTotal * tarifaNormal)
                + (minutosExcedidos * tarifaPausaExcedida);
    }

    public Tarifa obtenerTarifa(String tipo) {
        return tarifaRepository.findByTipo(tipo)
                .orElseThrow(() -> new NotFoundException("Tarifa " + tipo + " no encontrada"));
    }

    public Tarifa actualizarTarifa(String tipo, Float nuevoValor) {
        Tarifa tarifa = tarifaRepository.findByTipo(tipo)
                .orElseThrow(() -> new NotFoundException("Tarifa " + tipo + " no encontrada"));
        
        tarifa.setValor(nuevoValor);
        return tarifaRepository.save(tarifa);
    }

    public Tarifa crearTarifa(String tipo, Float valor, Integer pausaMaxMinutos) {
        Tarifa tarifa = Tarifa.builder()
                .tipo(tipo)
                .valor(valor)
                .pausaMaxMinutos(pausaMaxMinutos)
                .build();
        return tarifaRepository.save(tarifa);
    }
}
