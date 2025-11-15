package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.dto.request.TarifaRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@FeignClient(name = "viajes-service", url = "http://localhost:8086/api")
public interface ViajesClient {

    @GetMapping("/viajes/monopatines/mas-viajes")
    List<MonopatinResponseDTO> getMonopatinesConMasViajes(@RequestParam("anio") int anio, @RequestParam("minViajes") long minViajes);

    @GetMapping("/reportes/total-facturado")
    Float getTotalFacturado(
            @RequestParam("anio") int anio,
            @RequestParam("mesInicio") int mesInicio,
            @RequestParam("mesFin") int mesFin
    );

    @PutMapping("tarifas/{tipo}")
    void actualizarTarifa(
            @PathVariable("tipo") String tipo,
            @RequestBody TarifaRequestDTO request
    );


}
