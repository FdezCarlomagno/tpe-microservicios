package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@FeignClient(name="monopatin-service", url = "http://localhost:8083/api/monopatines")
public interface MonopatinClient {
    @GetMapping
    List<MonopatinResponseDTO>listarMonopatines();
    @PostMapping
    MonopatinResponseDTO crear(@RequestBody MonopatinRequestDTO dto);
    @PutMapping("/{id}")
    MonopatinResponseDTO actualizar(@PathVariable("id") Long id, @RequestBody MonopatinRequestDTO dto);
    @DeleteMapping("/{id}")
    void eliminar(@PathVariable("id") Long id);

}
