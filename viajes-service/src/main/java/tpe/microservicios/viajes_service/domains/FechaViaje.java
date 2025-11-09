package tpe.microservicios.viajes_service.domains;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.LocalTime;

@Embeddable
public record FechaViaje(
        LocalDate fechaInicioViaje,
        LocalTime horarioInicioViaje,
        LocalDate fechaFinViaje,
        LocalTime horarioFinViaje
) {
}
