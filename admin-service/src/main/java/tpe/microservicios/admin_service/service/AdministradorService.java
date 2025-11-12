package tpe.microservicios.admin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.admin_service.clients.MonopatinClient;
import tpe.microservicios.admin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.admin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorService {
    private final MonopatinClient monopatinClient;
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

}
