package tpe.microservicios.viajes_service.service.dto.response;

public record TarifaResponseDTO(
        Long id,
        String tipo,
        Float valor,
        Integer pausaMaxMinutos
) {}
