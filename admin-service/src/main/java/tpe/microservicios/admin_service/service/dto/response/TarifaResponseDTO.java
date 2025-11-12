package tpe.microservicios.admin_service.service.dto.response;

public record TarifaResponseDTO(
        Long id,
        String tipo,
        Float valor,
        Integer pausaMaxMinutos
) {}
