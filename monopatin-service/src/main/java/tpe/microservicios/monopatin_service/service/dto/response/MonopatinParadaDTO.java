package tpe.microservicios.monopatin_service.service.dto.response;

public record MonopatinParadaDTO(
        long idParada,
        long idMonopatin,
        String parada
) {
}