package tpe.microservicios.monopatin_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.monopatin_service.service.MonopatinService;
import tpe.microservicios.monopatin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/monopatines")
@RequiredArgsConstructor
@Tag(name = "Monopatines", description = "API para gestión de monopatines")
public class MonopatinController {

    private final MonopatinService monopatinService;

    @GetMapping
    @Operation(summary = "Obtener todos los monopatines")
    @ApiResponse(responseCode = "200", description = "Lista de monopatines obtenida exitosamente")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatines() {
        return ResponseEntity.ok(monopatinService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener monopatin por ID")
    @ApiResponse(responseCode = "200", description = "Monopatin encontrado")
    @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    public ResponseEntity<MonopatinResponseDTO> getMonopatinById(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo monopatin")
    @ApiResponse(responseCode = "201", description = "Monopatin creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    public ResponseEntity<MonopatinResponseDTO> addMonopatin(
            @Valid @RequestBody MonopatinRequestDTO monopatinRequestDTO) {
        MonopatinResponseDTO created = monopatinService.agregarMonopatin(monopatinRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // ✅ 201 CREATED
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un monopatin")
    @ApiResponse(responseCode = "200", description = "Monopatin actualizado")
    @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    public ResponseEntity<MonopatinResponseDTO> updateMonopatin(
            @PathVariable Long id,
            @Valid @RequestBody MonopatinRequestDTO monopatinRequestDTO) {
        return ResponseEntity.ok(monopatinService.actualizarMonopatin(id, monopatinRequestDTO));
    }

    @PatchMapping("/{id}/mantenimiento")  // ✅ PATCH para actualizaciones parciales
    @Operation(summary = "Registrar monopatin en mantenimiento")
    @ApiResponse(responseCode = "200", description = "Monopatin enviado a mantenimiento")
    @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    public ResponseEntity<String> registrarEnMantenimiento(@PathVariable Long id) {
        monopatinService.registrarMonopatinEnMantenimiento(id);
        return ResponseEntity.ok("Actualizado correctamente");
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar un monopatin")
    public ResponseEntity<MonopatinResponseDTO> activarMonopatin(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.activarMonopatin(id));
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un monopatin")
    public ResponseEntity<MonopatinResponseDTO> desactivarMonopatin(@PathVariable Long id) {
        return ResponseEntity.ok(monopatinService.desactivarMonopatin(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un monopatin")
    @ApiResponse(responseCode = "204", description = "Monopatin eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    public ResponseEntity<Void> eliminarMonopatin(@PathVariable Long id) {
        monopatinService.quitarMonopatin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/parada/{id}")
    @Operation(summary = "Setear la parada de un monopatin")
    @ApiResponse(responseCode = "200", description = "Monopatin actualizado correctamente")
    @ApiResponse(responseCode = "404", description = "Monopatin no encontrado")
    public ResponseEntity<MonopatinResponseDTO> actualizarParada(@PathVariable Long idMonopatin, @RequestBody Long idParada) {
        return ResponseEntity.ok(monopatinService.actualizarParada(idParada, idMonopatin));
    }


    // Endpoints adicionales útiles
    @GetMapping("/disponibles")
    @Operation(summary = "Obtener monopatines disponibles")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesDisponibles() {
        return ResponseEntity.ok(monopatinService.findAllDisponibles());
    }

    @GetMapping("/parada/{idParada}")
    @Operation(summary = "Obtener monopatines por parada")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesByParada(
            @PathVariable Long idParada) {
        return ResponseEntity.ok(monopatinService.findByParada(idParada));
    }
}