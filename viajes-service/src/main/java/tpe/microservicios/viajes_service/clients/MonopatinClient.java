package tpe.microservicios.viajes_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tpe.microservicios.viajes_service.service.dto.response.MonopatinResponseDTO;

@FeignClient(name = "monopatin-service", url = "http://localhost:8083/api/monopatines")
public interface MonopatinClient {

    @GetMapping("/{id}")
    MonopatinResponseDTO getMonopatinById(@PathVariable("id") long id);


}
