package tpe.microservicios.paradas_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinResponseDTO;

@FeignClient(name = "monopatin-service", url = "http://localhost:8083/api/monopatines")
public interface MonopatinClient {

    @GetMapping("/{id}")
    MonopatinResponseDTO getMonopatinById(@PathVariable("id") Long id);

    @PutMapping("/paradas/{id}")
    MonopatinResponseDTO registrarMonopatinParada(@PathVariable("id") Long idMonopatin, @RequestBody Long idParada);
}
