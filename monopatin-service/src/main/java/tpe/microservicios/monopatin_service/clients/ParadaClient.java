package tpe.microservicios.monopatin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.monopatin_service.service.dto.response.ParadaResponseDTO;

@FeignClient(name = "paradas-service", url = "http://localhost:8084/api/paradas")
public interface ParadaClient {

    @GetMapping("/{id}")
    ParadaResponseDTO getParadaById(@PathVariable("id") Long id);

    @PutMapping("/{id}/ubicar-monopatin")
    MonopatinResponseDTO ubicarMonopatinEnParada(
            @PathVariable("id") Long id,
            @RequestBody Long idMonopatin
    );
}
