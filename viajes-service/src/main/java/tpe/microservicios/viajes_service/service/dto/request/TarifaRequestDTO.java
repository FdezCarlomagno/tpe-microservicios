package tpe.microservicios.viajes_service.service.dto.request;

public record TarifaRequestDTO(
        String tipo,
        Float valor,
        Integer pausaMaxMinutos
) {}
