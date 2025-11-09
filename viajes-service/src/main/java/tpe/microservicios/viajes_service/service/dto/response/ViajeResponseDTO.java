package tpe.microservicios.viajes_service.service.dto.response;

import tpe.microservicios.viajes_service.utils.EstadoViaje;

import java.time.LocalDate;
import java.time.LocalTime;

public record ViajeResponseDTO(
        Long idViaje,
        Long idMonopatin,
        Long idUserAccount,
        Long idParadaOrigen,
        EstadoViaje estado,
        LocalDate fechaInicio,
        LocalTime horarioInicio
) {
}
