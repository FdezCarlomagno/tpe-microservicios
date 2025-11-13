package tpe.microservicios.reporte_service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import tpe.microservicios.reporte_service.dto.ReporteMonopatinesUsadosDTO;

import java.util.List;

@FeignClient(name = "viajes-service", url = "http://localhost:8087/api/viajes")
public interface ParadaClient {
    @GetMapping("/monopatines/usados")
    List<ReporteMonopatinesUsadosDTO> getMonopatinesUsados();
}
