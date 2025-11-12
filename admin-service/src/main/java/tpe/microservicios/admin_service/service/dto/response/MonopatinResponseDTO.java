package tpe.microservicios.admin_service.service.dto.response;

import lombok.Builder;

@Builder
public record MonopatinResponseDTO(
        Long id,
        String estado,
        float km,
        boolean disponible,
        Long idParada
) {
}
