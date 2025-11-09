package tpe.microservicios.monopatin_service.service.dto.response;

public record MonopatinResponseDTO(
        long id,
        String parada,
        boolean disponible
) {
}
