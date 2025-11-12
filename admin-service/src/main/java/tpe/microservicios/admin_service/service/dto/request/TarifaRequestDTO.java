// src/main/java/tpe/microservicios/admin_service/service/dto/request/TarifaUpdateDTO.java
package tpe.microservicios.admin_service.service.dto.request;

import lombok.Data;

@Data
public class TarifaRequestDTO {
    private Float valor;
    private Integer pausaMaxMinutos; // solo si el tipo es "pausa"
}