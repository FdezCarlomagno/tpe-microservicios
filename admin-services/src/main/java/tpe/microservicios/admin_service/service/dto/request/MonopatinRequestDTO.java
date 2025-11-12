package tpe.microservicios.admin_service.service.dto.request;

import lombok.Builder;

@Builder
public record MonopatinRequestDTO(
        String estado,
        Long idParada
) {
}
