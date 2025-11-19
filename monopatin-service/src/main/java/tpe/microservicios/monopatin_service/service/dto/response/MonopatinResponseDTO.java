package tpe.microservicios.monopatin_service.service.dto.response;

import tpe.microservicios.monopatin_service.utils.EstadoMonopatin;

public record MonopatinResponseDTO(
        long id,
        String parada,
        boolean disponible,
        EstadoMonopatin estadoMonopatin
) {
}
