package tpe.microservicios.viajes_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.viajes_service.service.ViajeService;
import tpe.microservicios.viajes_service.service.dto.request.FinalizarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.PausarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.ViajeRequestDTO;
import tpe.microservicios.viajes_service.service.dto.response.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    // ============================================================
    // GET VIAJES
    // ============================================================
    @Operation(
            summary = "Obtener todos los viajes",
            description = "Retorna los datos de todos los viajes del sistema."
    )
    @ApiResponse(responseCode = "200", description = "Viaje encontrado")
    @GetMapping
    public ResponseEntity<List<ViajeResponseDTO>> getViajes() {
        return ResponseEntity.ok(viajeService.getViajes());
    }

    // ============================================================
    // GET VIAJE POR ID
    // ============================================================
    @Operation(
            summary = "Obtener un viaje por ID",
            description = "Retorna los datos de un viaje identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje encontrado"),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ViajeResponseDTO> getViajeById(
            @Parameter(name = "id", description = "ID del viaje", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(viajeService.getViajeById(id));
    }

    // ============================================================
    // CREAR VIAJE
    // ============================================================
    @Operation(
            summary = "Crear nuevo viaje",
            description = "Crea un viaje validando usuario, cuenta, monopatín y parada origen."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje creado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario / monopatín / parada no encontrados"),
            @ApiResponse(responseCode = "403", description = "Cuenta anulada")
    })
    @PostMapping
    public ResponseEntity<ViajeResponseDTO> addViaje(
            @RequestBody ViajeRequestDTO viajeRequestDTO
    ) {
        return ResponseEntity.ok(viajeService.addViaje(viajeRequestDTO));
    }

    // ============================================================
    // FINALIZAR VIAJE
    // ============================================================
    @Operation(
            summary = "Finalizar un viaje",
            description = "Finaliza un viaje, calcula costo, actualiza saldo y guarda la parada destino."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje finalizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Viaje / usuario / parada destino no encontrados"),
            @ApiResponse(responseCode = "403", description = "Cuenta anulada"),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente")
    })
    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ViajeResponseDTO> finalizarViaje(
            @Parameter(name = "id", description = "ID del viaje a finalizar", required = true)
            @PathVariable("id") Long id,
            @RequestBody FinalizarViajeDTO viaje
    ) {
        return ResponseEntity.ok(viajeService.finalizarViaje(id, viaje));
    }

    // ============================================================
    // PAUSAR VIAJE
    // ============================================================
    @Operation(
            summary = "Pausar un viaje en curso",
            description = "Pone el viaje en estado PAUSA si el usuario está habilitado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje pausado correctamente"),
            @ApiResponse(responseCode = "404", description = "Viaje o usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Cuenta anulada"),
            @ApiResponse(responseCode = "409", description = "El viaje ya está pausado")
    })
    @PutMapping("/{id}/pausar")
    public ResponseEntity<ViajeResponseDTO> pausarViaje(
            @Parameter(name = "id", description = "ID del viaje", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(viajeService.pausarViaje(id));
    }

    // ============================================================
    // REANUDAR VIAJE
    // ============================================================
    @Operation(
            summary = "Reanudar un viaje pausado",
            description = "Reanuda el último registro de pausa del viaje."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje reanudado correctamente"),
            @ApiResponse(responseCode = "404", description = "Viaje o usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "Cuenta anulada"),
            @ApiResponse(responseCode = "409", description = "El viaje no está en pausa")
    })
    @PutMapping("/{id}/reanudar")
    public ResponseEntity<ViajeResponseDTO> reanudarViaje(
            @Parameter(name = "id", description = "ID del viaje", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(viajeService.reanudarViaje(id));
    }

    // ============================================================
    // ELIMINAR VIAJE
    // ============================================================
    @Operation(
            summary = "Eliminar un viaje",
            description = "Elimina un viaje por ID si existe."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Viaje eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Viaje no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteViaje(
            @Parameter(name = "id", description = "ID del viaje", required = true)
            @PathVariable("id") Long id
    ) {
        viajeService.deleteViaje(id);
        return ResponseEntity.ok("Viaje eliminado correctamente");
    }

    // ============================================================
    // MONOPATINES CON MÁS VIAJES
    // ============================================================
    @Operation(
            summary = "Monopatines con más viajes",
            description = "Retorna los monopatines que superan cierto mínimo de viajes en un año."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado generado correctamente")
    })
    @GetMapping("/monopatines/mas-viajes")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesConMasViajes(
            @Parameter(name = "anio", description = "Año para el cálculo", required = true)
            @RequestParam int anio,
            @Parameter(name = "minViajes", description = "Mínimo de viajes", required = true)
            @RequestParam long minViajes
    ) {
        return ResponseEntity.ok(viajeService.getMonopatinesConMasDeXViajesEnAnio(anio, minViajes));
    }

    // ============================================================
    // TOTAL FACTURADO
    // ============================================================
    @Operation(
            summary = "Obtener total facturado",
            description = "Retorna el total facturado en un rango de meses dentro de un año."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cálculo exitoso")
    })
    @GetMapping("/reportes/total-facturado")
    public ResponseEntity<Float> getTotalFacturado(
            @Parameter(name = "anio", required = true, description = "Año")
            @RequestParam(name = "anio") int anio,
            @Parameter(name = "mesInicio", required = true, description = "Mes inicial (1–12)")
            @RequestParam(name = "mesInicio") int mesInicio,
            @Parameter(name = "mesFin", required = true, description = "Mes final (1–12)")
            @RequestParam int mesFin
    ) {
        return ResponseEntity.ok(viajeService.getTotalFacturadoEnRangoMeses(anio, mesInicio, mesFin));
    }

    // ============================================================
    // USUARIOS MÁS ACTIVOS
    // ============================================================
    @Operation(
            summary = "Usuarios más activos",
            description = "Retorna los usuarios que más viajes realizaron en un período, filtrados por tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado generado correctamente")
    })
    @GetMapping("/reportes/usuarios-mas-activos")
    public ResponseEntity<List<UserUsageDTO>> getUsuariosMasActivos(
            @Parameter(name = "tipoUsuario", required = true, description = "Tipo de usuario (ADMIN, NORMAL, etc.)")
            @RequestParam(name = "tipoUsuario") String tipoUsuario,
            @Parameter(name = "fechaInicio", required = true, description = "Fecha inicio (AAAA-MM-DD)")
            @RequestParam(name = "fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @Parameter(name = "fechaFin", required = true, description = "Fecha fin (AAAA-MM-DD)")
            @RequestParam(name = "fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return ResponseEntity.ok(viajeService.getUsuariosMasActivosPorTipo(fechaInicio, fechaFin, tipoUsuario));
    }

    // ============================================================
    // USO DE MONOPATINES
    // ============================================================
    @Operation(
            summary = "Uso de monopatines por usuario",
            description = "Retorna los kilómetros recorridos por un usuario (y opcionalmente sus relacionados)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")
    })
    @GetMapping("/reportes/uso")
    public ResponseEntity<KmRecorridosDTO> getUsoMonopatines(
            @Parameter(name = "idUserAccount", description = "ID del usuario", required = true)
            @RequestParam(name = "idUserAccount") Long idUserAccount,
            @Parameter(name = "fechaInicio", description = "Fecha inicio (AAAA-MM-DD)", required = true)
            @RequestParam(name = "fechaInicio") LocalDate fechaInicio,
            @Parameter(name = "fechaFin", description = "Fecha fin (AAAA-MM-DD)", required = true)
            @RequestParam LocalDate fechaFin,
            @Parameter(name = "incluirRelacionados", description = "Incluir cuentas relacionadas (true/false)")
            @RequestParam(name = "incluirRelacionados", defaultValue = "false") boolean incluirRelacionados
    ) {
        return ResponseEntity.ok(viajeService.getUsoMonopatines(idUserAccount, fechaInicio, fechaFin, incluirRelacionados));
    }
}
