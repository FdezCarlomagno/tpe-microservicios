package tpe.microservicios.paradas_service.web;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


    @Operation(
            summary = "Obtener todas las paradas del sistema",
            description = "Obtiene todas las paradas del sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de paradas obtenida"
    )
    @GetMapping
    public ResponseEntity<List<ParadaResponseDTO>> getParadas(){
        return ResponseEntity.ok(paradasService.getParadas());
    }

    @Operation(
            summary = "Obtener parada por id",
            description = "Obtiene una parada por su id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Parada obtenida correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parada no encontrada"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParadaResponseDTO> getParadaById(
            @Parameter(description = "ID de la parada", example = "4")
            @PathVariable("id") long id){
        ParadaResponseDTO paradaResponseDTO = paradasService.getParadaById(id);
        if (paradaResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paradaResponseDTO);
    }

    @Operation(
            summary = "Registrar una parada en el sistema",
            description = "Registra una parada en el sistema"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Parada correctamente registrada"
    )
    @PostMapping
    public ResponseEntity<ParadaResponseDTO> registrarParada(
            @RequestBody @Valid ParadaRequestDTO parada){
        return ResponseEntity.status(HttpStatus.CREATED).body(paradasService.registrarParada(parada));
    }

    @Operation(
            summary = "Actualizar una parada",
            description = "Actualiza los datos de una parada especifica del sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Parada correctamente actualizada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parada no encontrada"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ParadaResponseDTO> updateParada(
            @PathVariable("id") long id,
            @RequestBody @Valid ParadaRequestDTO parada){
        ParadaResponseDTO p = paradasService.updateParada(id, parada);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(p);
    }


    @Operation(
            summary = "Eliminar una parada",
            description = "Elimina una parada del sistema"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Parada eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parada a eliminar no encontrada"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ParadaResponseDTO> deleteParada(
            @Parameter(description = "ID de la parada", example = "2")
            @PathVariable("id") long id){
        paradasService.quitarParada(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ubicar monopatin en parada",
            description = "Ubica un monopatin en una parada seleccionada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Monopatin correctamente ubicado en la parada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Monopatin a ubicar no encontrado"
            )
    })
    @PutMapping("/{id}/ubicar-monopatin")
    public ResponseEntity<MonopatinParadaDTO> ubicarMonopatinEnParada(@PathVariable("id") Long idParada, @RequestBody @Valid Long idMonopatin){
        MonopatinParadaDTO m = paradasService.ubicarMonopatinEnParada(idParada, idMonopatin);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(m);
    }

    @Operation(
            summary = "Remover monopatin de parada",
            description = "Remueve un monopatin de una parada seleccionada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Monopatin correctamente removido de la parada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Monopatin a remover no encontrado"
            )
    })
    @PutMapping("/{id}/remover-monopatin")
    public ResponseEntity<MonopatinParadaDTO> removerMopatinDeParada(@PathVariable("id") Long idParada, @RequestBody @Valid Long idMonopatin){
        MonopatinParadaDTO m = paradasService.removerMonopatinDeParada(idParada, idMonopatin);
        if (m == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(m);
    }

    @Operation(
            summary = "Obtener monopatines por parada",
            description = "Obtiene todos los monopatines ubicados en una parada especificada"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de monopatines obtenida"
    )
    @GetMapping("/{id}/monopatines")
    public ResponseEntity<List<MonopatinParadaDTO>> getMonopatinesByParada(@PathVariable("id") Long idParada){
        return ResponseEntity.ok(paradasService.getMonopatinesByParada(idParada));
    }
}
