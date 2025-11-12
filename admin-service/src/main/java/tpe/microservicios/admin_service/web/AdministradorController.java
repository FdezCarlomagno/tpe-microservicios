package tpe.microservicios.admin_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.AdministradorService;
import tpe.microservicios.admin_service.service.dto.request.*;
import tpe.microservicios.admin_service.service.dto.response.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    // ==================== MONOPATINES ====================

    @GetMapping("/monopatines")
    public List<MonopatinResponseDTO> listarMonopatines() {
        return administradorService.listarMonopatines();
    }

    @PostMapping("/monopatines")
    public MonopatinResponseDTO crearMonopatin(@RequestBody MonopatinRequestDTO dto) {
        return administradorService.crearMonopatin(dto);
    }

    @PutMapping("/monopatines/{id}")
    public MonopatinResponseDTO actualizarMonopatin(
            @PathVariable Long id,
            @RequestBody MonopatinRequestDTO dto) {
        return administradorService.actualizarMonopatin(id, dto);
    }

    @DeleteMapping("/monopatines/{id}")
    public ResponseEntity<Void> eliminarMonopatin(@PathVariable Long id) {
        administradorService.eliminarMonopatin(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PARADAS ====================

    @GetMapping("/paradas")
    public List<ParadaResponseDTO> listarParadas() {
        return administradorService.listarParadas();
    }

    @PostMapping("/paradas")
    public ParadaResponseDTO crearParada(@RequestBody ParadaRequestDTO dto) {
        return administradorService.crearParada(dto);
    }

    @PutMapping("/paradas/{id}")
    public ParadaResponseDTO actualizarParada(
            @PathVariable Long id,
            @RequestBody ParadaRequestDTO dto) {
        return administradorService.actualizarParada(id, dto);
    }

    @DeleteMapping("/paradas/{id}")
    public ResponseEntity<Void> eliminarParada(@PathVariable Long id) {
        administradorService.eliminarParada(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== TARIFAS ====================

    @PutMapping("/tarifas/{tipo}")
    public ResponseEntity<TarifaResponseDTO> actualizarTarifas(@PathVariable String tipo, @RequestBody TarifaRequestDTO request) {
        administradorService.actualizarTarifa(tipo, request.getValor());
        TarifaResponseDTO response = new TarifaResponseDTO(null, tipo, request.getValor(), null);
        return ResponseEntity.ok(response);
    }

    // ==================== CUENTAS ====================

    @PostMapping("/cuentas/{id}/anular")
    public ResponseEntity<Void> anularCuenta(@PathVariable Long id) {
        administradorService.anularCuenta(id);
        return ResponseEntity.ok().build();
    }
}