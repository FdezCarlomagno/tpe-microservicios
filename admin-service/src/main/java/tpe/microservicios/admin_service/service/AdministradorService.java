package tpe.microservicios.admin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.admin_service.clients.AccountClient;
import tpe.microservicios.admin_service.clients.MonopatinClient;
import tpe.microservicios.admin_service.clients.ViajesClient;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorService {
    private final MonopatinClient monopatinClient;
    private final AccountClient accountClient;
    private final ViajesClient  viajesClient;

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

}
