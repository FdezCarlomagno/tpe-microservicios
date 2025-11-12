package tpe.microservicios.admin_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.AdministradorService;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping("/monopatines")
    public ResponseEntity<List<MonopatinResponseDTO>> listar() {
        return ResponseEntity.ok(administradorService.listarMonopatines());
    }

    @PostMapping("/monopatines")
    public ResponseEntity<MonopatinResponseDTO> crear(@RequestBody MonopatinRequestDTO dto) {
        return ResponseEntity.ok(administradorService.crearMonopatin(dto));
    }
    /**
     * b. Como administrador quiero poder anular cuentas de usuarios, para inhabilitar el uso
     * momentáneo de la aplicación
     * */
    @PutMapping("/usuarios/anular-cuenta/{id}")
    public ResponseEntity<AccountResponseDTO> anularCuenta(@PathVariable("id") Long idAccount) {
        return ResponseEntity.ok(administradorService.anularCuenta(idAccount));
    }

    @PutMapping("/monopatines/{id}")
    public ResponseEntity<MonopatinResponseDTO> actualizar(@PathVariable Long id, @RequestBody MonopatinRequestDTO dto) {
        return ResponseEntity.ok(administradorService.actualizarMonopatin(id, dto));
    }

    @DeleteMapping("/monopatines/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        administradorService.eliminarMonopatin(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/viajes/reporte/mas-viajes")
    public ResponseEntity<List<MonopatinResponseDTO>> getMonopatinesConMasViajes(
            @RequestParam int anio,
            @RequestParam long minViajes
    ){
        return ResponseEntity.ok(administradorService.getMonopatinesConMasViajes(anio, minViajes));
    }

    @GetMapping("/viajes/reporte/factura-viajes")
    public ResponseEntity<Float> getTotalFacturadoViajes(
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin
    ){
        return ResponseEntity.ok(administradorService.getTotalFacturadoViajes(anio, mesInicio, mesFin));
    }
}
