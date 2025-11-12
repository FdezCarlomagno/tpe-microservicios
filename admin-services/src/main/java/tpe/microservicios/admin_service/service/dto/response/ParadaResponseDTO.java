// src/main/java/tpe/microservicios/admin_service/service/dto/response/ParadaResponseDTO.java
package tpe.microservicios.admin_service.service.dto.response;

import lombok.Data;

@Data
public class ParadaResponseDTO {
    private Long id;
    private String ubicacion;
    private Double latitud;
    private Double longitud;
    private Integer capacidad;
}