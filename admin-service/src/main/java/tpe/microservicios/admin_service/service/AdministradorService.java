package tpe.microservicios.admin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tpe.microservicios.admin_service.clients.MonopatinClient;
import tpe.microservicios.admin_service.clients.ParadaClient;
import tpe.microservicios.admin_service.clients.TarifaClient;
import tpe.microservicios.admin_service.clients.UserClient;
import tpe.microservicios.admin_service.service.dto.request.*;
import tpe.microservicios.admin_service.service.dto.response.*;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorService {

    private final MonopatinClient monopatinClient;
    private final ParadaClient paradaClient;
    private final UserClient cuentaClient; // ← Cliente para anular cuentas
    private final TarifaClient tarifaClient;

    // ==================== MONOPATINES ====================

    public List<MonopatinResponseDTO> listarMonopatines() {
        return monopatinClient.listarMonopatines();
    }

    public MonopatinResponseDTO crearMonopatin(MonopatinRequestDTO dto) {
        return monopatinClient.crear(dto);
    }

    public MonopatinResponseDTO actualizarMonopatin(Long id, MonopatinRequestDTO dto) {
        return monopatinClient.actualizar(id, dto);
    }

    public void eliminarMonopatin(Long id) {
        monopatinClient.eliminar(id);
    }

    // ==================== PARADAS ====================

    public List<ParadaResponseDTO> listarParadas() {
        return paradaClient.listarParadas();
    }

    public ParadaResponseDTO crearParada(ParadaRequestDTO dto) {
        return paradaClient.crearParada(dto);
    }

    public ParadaResponseDTO actualizarParada(Long id, ParadaRequestDTO dto) {
        return paradaClient.actualizarParada(id, dto);
    }

    public void eliminarParada(Long id) {
        paradaClient.eliminarParada(id);
    }

    // ==================== TARIFAS ====================

    public void actualizarTarifa(String tipo, Float nuevoValor) {
        var request = new TarifaRequestDTO();
        request.setValor(nuevoValor);
        tarifaClient.actualizarTarifa(tipo, request);
    }



    // ==================== CUENTAS ====================

    public void anularCuenta(Long cuentaId) {
        cuentaClient.anular(cuentaId);
    }
}