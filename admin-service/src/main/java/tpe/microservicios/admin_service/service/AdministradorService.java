package tpe.microservicios.admin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.admin_service.clients.*;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.admin_service.service.dto.request.TarifaRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.admin_service.service.dto.response.ParadaResponseDTO;


import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorService {
    private final MonopatinClient monopatinClient;
    private final AccountClient accountClient;
    private final ViajesClient  viajesClient;
    private final ParadaClient paradaClient;

    public List<MonopatinResponseDTO> listarMonopatines(){
        return  monopatinClient.listarMonopatines();
    }
    public MonopatinResponseDTO crearMonopatin(MonopatinRequestDTO monopatin){
        return monopatinClient.crear(monopatin);
    }
    public MonopatinResponseDTO actualizarMonopatin(Long id, MonopatinRequestDTO monopatin){
        return monopatinClient.actualizar(id, monopatin);
    }
    public void eliminarMonopatin(Long id){
        monopatinClient.eliminar(id);
    }

    public AccountResponseDTO anularCuenta(Long idCuenta){
        var account = accountClient.getAccount(idCuenta);

        if(account == null){
            throw new RuntimeException("No se encontro la cuenta");
        }

        return accountClient.anularCuenta(account.idAccount());
    }

    public List<MonopatinResponseDTO> getMonopatinesConMasViajes(int anio, long minViajes){
        return viajesClient.getMonopatinesConMasViajes(anio, minViajes);
    }

    public Float getTotalFacturadoViajes(int anio, int mesInicio, int mesFin){
        return viajesClient.getTotalFacturado(anio, mesInicio, mesFin);
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
        viajesClient.actualizarTarifa(tipo, request);
    }

}
