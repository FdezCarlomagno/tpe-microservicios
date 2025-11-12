// src/main/java/tpe/microservicios/admin_service/service/dto/request/ParadaRequestDTO.java
package tpe.microservicios.admin_service.service.dto.request;

import lombok.Data;

@Data
public class ParadaRequestDTO {
    private String ubicacion;
    private Double latitud;
    private Double longitud;
    private Integer capacidad;
}