package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.ParadaResponseDTO;

import java.util.List;

@FeignClient(name = "parada-service", url = "http://localhost:8084/api/paradas")
public interface ParadaClient {

    @GetMapping
    List<ParadaResponseDTO> listarParadas();

    @PostMapping
    ParadaResponseDTO crearParada(@RequestBody ParadaRequestDTO dto);

    @PutMapping("/{id}")
    ParadaResponseDTO actualizarParada(
            @PathVariable("id") Long id,
            @RequestBody ParadaRequestDTO dto
    );

    @DeleteMapping("/{id}")
    void eliminarParada(@PathVariable("id") Long id);
}