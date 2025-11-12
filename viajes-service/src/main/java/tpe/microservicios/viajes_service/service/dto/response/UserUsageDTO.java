package tpe.microservicios.viajes_service.service.dto.response;

public record UserUsageDTO(
        Long idUserAccount,
        String tipoUsuario,
        Long cantidadViajes
) {}
