package tpe.microservicios.viajes_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tpe.microservicios.viajes_service.domains.Tarifa;
import tpe.microservicios.viajes_service.repository.TarifaRepository;

@Component
@RequiredArgsConstructor
public class TarifaInitializer implements CommandLineRunner {

    private final TarifaRepository tarifaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar tarifas por defecto si no existen
        if (tarifaRepository.findByTipo("NORMAL").isEmpty()) {
            Tarifa tarifaNormal = Tarifa.builder()
                    .tipo("NORMAL")
                    .valor(1.0f)
                    .pausaMaxMinutos(null)
                    .build();
            tarifaRepository.save(tarifaNormal);
        }

        if (tarifaRepository.findByTipo("PAUSA_EXCEDIDA").isEmpty()) {
            Tarifa tarifaPausaExcedida = Tarifa.builder()
                    .tipo("PAUSA_EXCEDIDA")
                    .valor(2.0f)
                    .pausaMaxMinutos(null)
                    .build();
            tarifaRepository.save(tarifaPausaExcedida);
        }

        if (tarifaRepository.findByTipo("REANUDADA").isEmpty()) {
            Tarifa tarifaReanudada = Tarifa.builder()
                    .tipo("REANUDADA")
                    .valor(1.5f)
                    .pausaMaxMinutos(null)
                    .build();
            tarifaRepository.save(tarifaReanudada);
        }

        if (tarifaRepository.findByTipo("PAUSA_MAX_MINUTOS").isEmpty()) {
            Tarifa pausaMaxMinutos = Tarifa.builder()
                    .tipo("PAUSA_MAX_MINUTOS")
                    .valor(0f)
                    .pausaMaxMinutos(15)
                    .build();
            tarifaRepository.save(pausaMaxMinutos);
        }
    }
}
