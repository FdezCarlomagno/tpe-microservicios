package tpe.microservicios.paradas_service.service.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

public record ParadaRequestDTO(
        long id,
         String nombreParada,
         String descripcionParada,
         String direccionParada,
         List<Long>idMonopatines
) {
}
