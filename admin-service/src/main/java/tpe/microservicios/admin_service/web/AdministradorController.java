package tpe.microservicios.admin_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tpe.microservicios.admin_service.service.AdministradorService;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/admin/monopatines")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @GetMapping
    public List<MonopatinResponseDTO> listar() {
        return administradorService.listarMonopatines();
    }

    @PostMapping
    public MonopatinResponseDTO crear(@RequestBody MonopatinRequestDTO dto) {
        return administradorService.crearMonopatin(dto);
    }

    @PutMapping("/{id}")
    public MonopatinResponseDTO actualizar(@PathVariable Long id, @RequestBody MonopatinRequestDTO dto) {
        return administradorService.actualizarMonopatin(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.eliminarMonopatin(id);
    }
}
