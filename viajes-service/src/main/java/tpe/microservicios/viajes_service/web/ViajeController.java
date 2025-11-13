package tpe.microservicios.viajes_service.web;


import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.viajes_service.service.ViajeService;
import tpe.microservicios.viajes_service.service.dto.request.FinalizarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.PausarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.ViajeRequestDTO;
import tpe.microservicios.viajes_service.service.dto.response.KmRecorridosDTO;
import tpe.microservicios.viajes_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.UserUsageDTO;
import tpe.microservicios.viajes_service.service.dto.response.ViajeResponseDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> getViajeById(@PathVariable("id") Long id){
        return ResponseEntity.ok(viajeService.getViajeById(id));
    }

    @PostMapping
    public ResponseEntity<ViajeResponseDTO> addViaje(@RequestBody ViajeRequestDTO viajeRequestDTO){
        return ResponseEntity.ok(viajeService.addViaje(viajeRequestDTO));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(@PathVariable("id") Long idViaje, @RequestBody FinalizarViajeDTO viaje){
        return ResponseEntity.ok(viajeService.finalizarViaje(idViaje, viaje));
    }

    @PutMapping("/{id}/pausar")
    public ResponseEntity<ViajeResponseDTO> pausarViaje(@PathVariable("id") Long idViaje, @RequestBody PausarViajeDTO pausarViajeDTO){
        return ResponseEntity.ok(viajeService.pausarViaje(idViaje, pausarViajeDTO.idAccount()));
    }

    @PutMapping("/{id}/reanudar")
    public ResponseEntity<ViajeResponseDTO> reanudarViaje(@PathVariable("id") Long idViaje, @RequestBody PausarViajeDTO pausarViajeDTO){
        return ResponseEntity.ok(viajeService.reanudarViaje(idViaje, pausarViajeDTO.idAccount()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteViaje(@PathVariable("id") Long idViaje){
        viajeService.deleteViaje(idViaje);
        return ResponseEntity.ok("Viaje eliminado correctamente");
    }

    @GetMapping("/monopatines/mas-viajes")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesConMasViajes(
            @RequestParam int anio,
            @RequestParam long minViajes
    ) {
        return ResponseEntity.ok(viajeService.getMonopatinesConMasDeXViajesEnAnio(anio, minViajes));
    }

    @GetMapping("/reportes/total-facturado")
    public ResponseEntity<Float> getTotalFacturado(
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin
    ) {
        return ResponseEntity.ok(viajeService.getTotalFacturadoEnRangoMeses(anio, mesInicio, mesFin));
    }

    @GetMapping("/reportes/usuarios-mas-activos")
    public ResponseEntity<List<UserUsageDTO>> getUsuariosMasActivos(
            @RequestParam String tipoUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return ResponseEntity.ok(viajeService.getUsuariosMasActivosPorTipo(fechaInicio, fechaFin, tipoUsuario));
    }


    @GetMapping("/reportes/uso")
    public ResponseEntity<KmRecorridosDTO> getUsoMonopatines(
            @RequestParam Long idUserAccount,
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin,
            @RequestParam(defaultValue = "false") boolean incluirRelacionados
    ){
        return ResponseEntity.ok(viajeService.getUsoMonopatines(idUserAccount, fechaInicio, fechaFin, incluirRelacionados));
    }
}
