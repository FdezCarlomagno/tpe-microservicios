// src/main/java/tpe/microservicios/admin_service/clients/TarifaClient.java
package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.dto.request.TarifaRequestDTO;

@FeignClient(name = "viajes-service", url = "http://localhost:8081/api/tarifas")
public interface TarifaClient {

    @PutMapping("/{tipo}")
    void actualizarTarifa(
            @PathVariable("tipo") String tipo,
            @RequestBody TarifaRequestDTO request
    );
}