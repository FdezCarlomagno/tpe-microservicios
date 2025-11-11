package tpe.microservicios.viajes_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {

    private Long idMonopatin;

    private float km;
}
