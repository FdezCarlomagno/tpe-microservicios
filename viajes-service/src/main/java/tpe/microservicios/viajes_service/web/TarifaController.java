package tpe.microservicios.viajes_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    // ============================================================
    // OBTENER TODAS LAS TARIFAS
    // ============================================================
    @Operation(
            summary = "Obtener todas las tarifas",
            description = "Devuelve la lista completa de tarifas configuradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de tarifas obtenido correctamente")
    })
    @GetMapping
    public ResponseEntity<List<TarifaResponseDTO>> obtenerTodasLasTarifas() {
        List<Tarifa> tarifas = tarifaRepository.findAll();
        List<TarifaResponseDTO> response = tarifas.stream()
                .map(t -> new TarifaResponseDTO(t.getId(), t.getTipo(), t.getValor(), t.getPausaMaxMinutos()))
                .toList();
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // OBTENER TARIFA POR TIPO
    // ============================================================
    @Operation(
            summary = "Obtener una tarifa por tipo",
            description = "Devuelve una tarifa específica buscándola por su tipo (NORMAL, PAUSA_EXCEDIDA, etc.)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no encontrada",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{tipo}")
    public ResponseEntity<TarifaResponseDTO> obtenerTarifa(
            @Parameter(name = "tipo", description = "Tipo de tarifa", required = true)
            @PathVariable String tipo
    ) {
        Tarifa tarifa = tarifaService.obtenerTarifa(tipo);
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // CREAR TARIFA
    // ============================================================
    @Operation(
            summary = "Crear nueva tarifa",
            description = "Crea una nueva tarifa. Solo debe ser usado por administradores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarifa creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<TarifaResponseDTO> crearTarifa(
            @RequestBody TarifaRequestDTO request
    ) {
        Tarifa tarifa = tarifaService.crearTarifa(request.tipo(), request.valor(), request.pausaMaxMinutos());
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ============================================================
    // ACTUALIZAR TARIFA
    // ============================================================
    @Operation(
            summary = "Actualizar tarifa existente",
            description = "Actualiza el valor de una tarifa existente por tipo. Solo administradores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Tarifa no encontrada",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{tipo}")
    public ResponseEntity<TarifaResponseDTO> actualizarTarifa(
            @Parameter(name = "tipo", description = "Tipo de tarifa a actualizar", required = true)
            @PathVariable String tipo,
            @RequestBody TarifaRequestDTO request
    ) {
        Tarifa tarifa = tarifaService.actualizarTarifa(tipo, request.valor());
        TarifaResponseDTO response = new TarifaResponseDTO(
                tarifa.getId(),
                tarifa.getTipo(),
                tarifa.getValor(),
                tarifa.getPausaMaxMinutos()
        );
        return ResponseEntity.ok(response);
    }

    // ============================================================
    // ELIMINAR TARIFA
    // ============================================================
    @Operation(
            summary = "Eliminar una tarifa",
            description = "Elimina una tarifa por su ID. Solo administradores."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarifa eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Tarifa no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(
            @Parameter(name = "id", description = "ID de la tarifa a eliminar", required = true)
            @PathVariable Long id
    ) {
        tarifaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
