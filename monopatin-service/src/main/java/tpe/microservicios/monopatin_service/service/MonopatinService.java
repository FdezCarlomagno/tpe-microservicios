package tpe.microservicios.monopatin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.monopatin_service.clients.ParadaClient;
import tpe.microservicios.monopatin_service.domain.Monopatin;
import tpe.microservicios.monopatin_service.repository.MonopatinRepository;
import tpe.microservicios.monopatin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.monopatin_service.service.dto.response.ParadaResponseDTO;
import tpe.microservicios.monopatin_service.utils.EstadoMonopatin;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MonopatinService {

    private final MonopatinRepository monopatinRepository;

    private final ParadaClient paradaClient;

    public List<MonopatinResponseDTO> findAll(){
        return monopatinRepository.findAll().stream().map(monopatin -> {
            var parada = paradaClient.getParadaById(monopatin.getIdParada());
            return new MonopatinResponseDTO(
                    monopatin.getId(),
                    parada.nombreParada(),
                    monopatin.getDisponible()
            );
        }).toList();
    }

    public MonopatinResponseDTO findById(Long id) {
        Monopatin m = monopatinRepository.findById(id).orElse(null);
        if (m == null) { return null;}

        return new MonopatinResponseDTO(
                m.getId(),
                paradaClient.getParadaById(m.getIdParada()).nombreParada(),
                m.getDisponible()
        );
    }

    public void registrarMonopatinEnMantenimiento(Long id){
        Monopatin m = monopatinRepository.findById(id).orElse(null);
        if (m == null || m.getEstado() == EstadoMonopatin.MANTENIMIENTO) return;

        m.enviarAMantenimiento();
        monopatinRepository.save(m);
    }

    public MonopatinResponseDTO agregarMonopatin(MonopatinRequestDTO monopatin){
        monopatinRepository.save(new Monopatin(monopatin));
        return new MonopatinResponseDTO(
                monopatin.id(),
                paradaClient.getParadaById(monopatin.idParada()).nombreParada(),
                monopatin.disponible()
        );
    }

    public void quitarMonopatin(Long id){
        monopatinRepository.deleteById(id);
    }

    public MonopatinResponseDTO actualizarParada(Long paradaId, Long idMonopatin){
        var parada = paradaClient.getParadaById(paradaId);
        if(parada == null) return null;

        Monopatin monopatin = monopatinRepository.findById(idMonopatin).orElse(null);
        if (monopatin == null) return null;

        monopatin.setIdParada(paradaId);
        monopatinRepository.save(monopatin);

        return new MonopatinResponseDTO(
                monopatin.getId(),
                parada.nombreParada(),
                monopatin.getDisponible()
        );
    }

    public MonopatinResponseDTO actualizarMonopatin(Long idMonopatin, MonopatinRequestDTO newMonopatin){
        Monopatin m =  monopatinRepository.findById(idMonopatin).orElse(null);
        if(m == null) throw new RuntimeException("Monopatin no encontrado");

        m.setIdParada(newMonopatin.idParada());
        m.setEstado(newMonopatin.estado());
        m.setKm(newMonopatin.km());
        m.setDisponible(newMonopatin.disponible());

        monopatinRepository.save(m);

        return new MonopatinResponseDTO(
                m.getId(),
                paradaClient.getParadaById(m.getIdParada()).nombreParada(),
                m.getDisponible()
        );
    }

    public MonopatinResponseDTO activarMonopatin(Long idMonopatin){
        Monopatin m =  monopatinRepository.findById(idMonopatin).orElse(null);
        if(m == null) throw new RuntimeException("Monopatin no encontrado");

        m.activar();
        monopatinRepository.save(m);
        return new MonopatinResponseDTO(
            m.getId(),
            paradaClient.getParadaById(m.getIdParada()).nombreParada(),
            m.getDisponible()
        );
    }

    public MonopatinResponseDTO desactivarMonopatin(Long idMonopatin){
        Monopatin m =  monopatinRepository.findById(idMonopatin).orElse(null);
        if(m == null) throw new RuntimeException("Monopatin no encontrado");

        m.desactivar();
        monopatinRepository.save(m);
        return new MonopatinResponseDTO(
                m.getId(),
                paradaClient.getParadaById(m.getIdParada()).nombreParada(),
                m.getDisponible()
        );
    }

    public List<MonopatinResponseDTO> findAllDisponibles(){
        return monopatinRepository.findAllDisponibles();
    }

    public List<MonopatinResponseDTO> findByParada(Long idParada){
        var parada = paradaClient.getParadaById(idParada);
        if(parada == null) throw new RuntimeException("Parada no encontrada");

        return parada.idMonopatines().stream().map(idMonopatin -> {
            Monopatin m =  monopatinRepository.findById(idMonopatin).orElse(null);
            if(m == null) return null;
            return new MonopatinResponseDTO(
                m.getId(),
                parada.nombreParada(),
                m.getDisponible()
            );
        }).filter(Objects::nonNull).toList();
    }
}
