package tpe.microservicios.paradas_service.service.dto.response;

public record MonopatinParadaDTO(
        long idParada,
        long idMonopatin,
        String parada
) {
}
