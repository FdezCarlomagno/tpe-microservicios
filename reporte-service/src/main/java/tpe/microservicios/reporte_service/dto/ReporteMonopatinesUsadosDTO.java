package tpe.microservicios.reporte_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteMonopatinesUsadosDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("km")
    private float km;



}
