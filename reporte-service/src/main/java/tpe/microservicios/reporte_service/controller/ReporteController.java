package tpe.microservicios.reporte_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tpe.microservicios.reporte_service.dto.ReporteMonopatinesUsadosDTO;
import tpe.microservicios.reporte_service.service.ReporteService;

import java.util.List;

/**
 * 4)
 * a. Como administrador quiero poder generar un reporte de uso de monopatines por kilómetros
 * para establecer si un monopatín requiere de mantenimiento. Este reporte debe poder
 * configurarse para incluir (o no) los tiempos de pausa.
 */

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/monopatin/usado")
    public ResponseEntity<List<ReporteMonopatinesUsadosDTO>> MonopatinesEnUsoInforme(){
        List<ReporteMonopatinesUsadosDTO> reportMonopatin = this.reporteService.getMonopatinUsado();
        if(reportMonopatin.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reportMonopatin);
    }


}
