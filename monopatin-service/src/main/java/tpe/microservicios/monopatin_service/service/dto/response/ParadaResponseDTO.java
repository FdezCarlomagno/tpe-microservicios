package tpe.microservicios.monopatin_service.service.dto.response;

import java.util.List;

public record ParadaResponseDTO(
        String nombreParada,
        String descripcionParada,
        String direccionParada,
        List<Long> idMonopatines
) {
}
