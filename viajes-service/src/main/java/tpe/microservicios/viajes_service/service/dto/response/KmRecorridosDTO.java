package tpe.microservicios.viajes_service.service.dto.response;

import java.util.List;

public record KmRecorridosDTO(
        Float kilometros,
        List<UserAccountResponseDTO> usuarios
) {
}
