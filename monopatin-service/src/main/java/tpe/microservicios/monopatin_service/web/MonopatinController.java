package tpe.microservicios.monopatin_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // ---------------------------------------------------------
    @GetMapping
    @Operation(summary = "Obtener todos los monopatines")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatines() {
        return ResponseEntity.ok(monopatinService.findAll());
    }

    // ---------------------------------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Obtener monopatín por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monopatín encontrado"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<MonopatinResponseDTO> getMonopatinById(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(monopatinService.findById(id));
    }

    // ---------------------------------------------------------
    @PostMapping
    @Operation(summary = "Crear nuevo monopatín")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<MonopatinResponseDTO> addMonopatin(
            @Valid @RequestBody MonopatinRequestDTO monopatinRequestDTO) {

        MonopatinResponseDTO created = monopatinService.agregarMonopatin(monopatinRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------------------------------------------------
    /*@PutMapping("/{id}")
    @Operation(summary = "Actualizar monopatín completo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<MonopatinResponseDTO> updateMonopatin(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id,

            @Valid @RequestBody MonopatinRequestDTO monopatinRequestDTO) {

        return ResponseEntity.ok(monopatinService.actualizarMonopatin(id, monopatinRequestDTO));
    }*/

    // ---------------------------------------------------------
    @PatchMapping("/{id}/mantenimiento")
    @Operation(summary = "Enviar monopatín a mantenimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<String> registrarEnMantenimiento(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id) {

        monopatinService.registrarMonopatinEnMantenimiento(id);
        return ResponseEntity.ok("Monopatín enviado a mantenimiento");
    }

    // ---------------------------------------------------------
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar monopatín")
    public ResponseEntity<MonopatinResponseDTO> activarMonopatin(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(monopatinService.activarMonopatin(id));
    }

    // ---------------------------------------------------------
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar monopatín")
    public ResponseEntity<MonopatinResponseDTO> desactivarMonopatin(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(monopatinService.desactivarMonopatin(id));
    }

    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar monopatín")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<Void> eliminarMonopatin(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id) {

        monopatinService.quitarMonopatin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/parada/{id}")
    @Operation(summary = "Actualizar parada de un monopatín")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<MonopatinResponseDTO> actualizarParada(
            @Parameter(name = "id", description = "ID del monopatín", required = true)
            @PathVariable("id") Long id,

            @Parameter(name = "idParada", description = "ID de la nueva parada", required = true)
            @RequestBody Long idParada) {

        return ResponseEntity.ok(monopatinService.actualizarParada(idParada, id));
    }

    @GetMapping("/paradas/{id}")
    public ResponseEntity<List<MonopatinResponseDTO>> findMonopatinesByParada(
            @Parameter(description = "ID de la parada", example = "5")
            @PathVariable("id") Long idParada
    ){
        return ResponseEntity.ok(monopatinService.findMonopatinesByParada(idParada));
    }

    @PutMapping("/{id}/remover-parada")
    @Operation(summary = "Remueve la parada asignada a un monopatín")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parada removida"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<MonopatinResponseDTO> removerMonopatinDeParada(
            @PathVariable("id") Long id) {

        return ResponseEntity.ok(monopatinService.removerDeParada(id));
    }


    // ---------------------------------------------------------
    @GetMapping("/disponibles")
    @Operation(summary = "Listar monopatines disponibles")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesDisponibles() {
        return ResponseEntity.ok(monopatinService.findAllDisponibles());
    }

    // ---------------------------------------------------------
    @GetMapping("/parada/{idParada}")
    @Operation(summary = "Listar monopatines por parada")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesByParada(
            @Parameter(name = "idParada", description = "ID de la parada", required = true)
            @PathVariable("idParada") Long idParada) {

        return ResponseEntity.ok(monopatinService.findByParada(idParada));
    }
}
