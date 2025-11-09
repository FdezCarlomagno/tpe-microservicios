package tpe.microservicios.viajes_service.service.dto.request;

public record FinalizarViajeDTO(
        float kilometros,
        Long idParadaDestino,
        Long idUserAccount
) {
}
