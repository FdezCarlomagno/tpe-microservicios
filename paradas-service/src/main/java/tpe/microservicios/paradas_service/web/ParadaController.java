package tpe.microservicios.paradas_service.web;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.paradas_service.service.ParadasService;
import tpe.microservicios.paradas_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinParadaDTO;
import tpe.microservicios.paradas_service.service.dto.response.ParadaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/paradas")
@RequiredArgsConstructor
public class ParadaController {

    private final ParadasService paradasService;

    @GetMapping
    public ResponseEntity<List<ParadaResponseDTO>> getParadas(){
        return ResponseEntity.ok(paradasService.getParadas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParadaResponseDTO> getParadaById(@PathVariable long id){
        return ResponseEntity.ok(paradasService.getParadaById(id));
    }

    @PostMapping
    public ResponseEntity<ParadaResponseDTO> registrarParada(ParadaRequestDTO parada){
        return ResponseEntity.ok(paradasService.registrarParada(parada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParadaResponseDTO> updateParada(@PathVariable long id, @RequestBody ParadaRequestDTO parada){
        return ResponseEntity.ok(paradasService.updateParada(id, parada));
    }

    @DeleteMapping
    public ResponseEntity<ParadaResponseDTO> deleteParada(long id){
        paradasService.quitarParada(id);
        return ResponseEntity.ok(paradasService.getParadaById(id));
    }

    /**
     * Ubicar monopatin en parada
     */
    @PutMapping("/{id}/ubicar-monopatin")
    public ResponseEntity<MonopatinParadaDTO> ubicarMonopatinEnParada(@PathVariable("id") Long idParada, @RequestBody Long idMonopatin){
        return ResponseEntity.ok(paradasService.ubicarMonopatinEnParada(idParada, idMonopatin));
    }

    @GetMapping("/{id}/monopatines")
    public ResponseEntity<List<MonopatinParadaDTO>> getMonopatinesByParada(@PathVariable("id") Long idParada){
        return ResponseEntity.ok(paradasService.getMonopatinesByParada(idParada));
    }
}
