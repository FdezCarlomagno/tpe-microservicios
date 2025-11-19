package tpe.microservicios.monopatin_service.service.dto.request;

import jakarta.persistence.*;
import tpe.microservicios.monopatin_service.utils.EstadoMonopatin;

public record MonopatinRequestDTO(
         EstadoMonopatin estado,
         boolean disponible,
         Long idParada,
         float km
) {
}
