package tpe.microservicios.paradas_service.service.dto.response;

import java.util.List;

public record ParadaResponseDTO(
        Long id,
        String nombreParada,
        String descripcionParada,
        String direccionParada,
        List<Long> idMonopatines
) {
}
