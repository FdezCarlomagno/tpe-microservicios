package tpe.microservicios.paradas_service.service.dto.response;

public record MonopatinResponseDTO(
        long id,
        String parada,
        boolean disponible
) {
}
