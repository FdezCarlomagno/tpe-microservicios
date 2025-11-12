package tpe.microservicios.viajes_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.viajes_service.domains.Tarifa;
import tpe.microservicios.viajes_service.repository.TarifaRepository;
import tpe.microservicios.viajes_service.service.TarifaService;
import tpe.microservicios.viajes_service.service.dto.request.TarifaRequestDTO;
import tpe.microservicios.viajes_service.service.dto.response.TarifaResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private final TarifaService tarifaService;
    private final TarifaRepository tarifaRepository;

    /**
     * Obtener todas las tarifas
     */
    @GetMapping
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTodasLasTarifas() {
        List<Tarifa> tarifas = tarifaRepository.findAll();
        List<TarifaResponseDTO> response = tarifas.stream()
                .map(t -> new TarifaResponseDTO(t.getId(), t.getTipo(), t.getValor(), t.getPausaMaxMinutos()))
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener una tarifa específica por tipo
     */
    @GetMapping("/{tipo}")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifa(@PathVariable String tipo) {
        Tarifa tarifa = tarifaService.obtenerTarifa(tipo);
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Crear una nueva tarifa (solo administrador)
     */
    @PostMapping
    public ResponseEntity<TarifaResponseDTO> crearTarifa(@RequestBody TarifaRequestDTO request) {
        Tarifa tarifa = tarifaService.crearTarifa(request.tipo(), request.valor(), request.pausaMaxMinutos());
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualizar una tarifa existente (solo administrador)
     */
    @PutMapping("/{tipo}")
    public ResponseEntity<TarifaResponseDTO> actualizarTarifa(
            @PathVariable String tipo,
            @RequestBody TarifaRequestDTO request) {
        Tarifa tarifa = tarifaService.actualizarTarifa(tipo, request.valor());
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar una tarifa (solo administrador)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id) {
        tarifaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
