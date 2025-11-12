package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@FeignClient(name = "viajes-service", url = "http://localhost:8087/api/viajes")
public interface ViajesClient {

    @GetMapping("/monopatines/mas-viajes")
    List<MonopatinResponseDTO> getMonopatinesConMasViajes(@RequestParam int anio, @RequestParam long minViajes);

    @GetMapping("/reportes/total-facturado")
    Float getTotalFacturado(
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin
    );


}
