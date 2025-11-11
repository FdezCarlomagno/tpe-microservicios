package tpe.microservicios.viajes_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tpe.microservicios.viajes_service.domains.FechaViaje;
import tpe.microservicios.viajes_service.dto.ViajeDTO;
import tpe.microservicios.viajes_service.service.ViajeService;

import java.util.List;

@RestController
@RequestMapping("/api/viaje")
public class ViajeController {
    @Autowired
    private ViajeService viajeService;

    @GetMapping("/monopatines/usados")
    public ResponseEntity<List<ViajeDTO>> getMonopatinesUsados(){
        return ResponseEntity.status(HttpStatus.OK).body(viajeService.getMonopatinUsado());
    }




}
