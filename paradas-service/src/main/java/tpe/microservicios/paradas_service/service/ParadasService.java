package tpe.microservicios.paradas_service.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tpe.microservicios.paradas_service.clients.MonopatinClient;
import tpe.microservicios.paradas_service.domain.Parada;
import tpe.microservicios.paradas_service.repository.ParadasRepository;
import tpe.microservicios.paradas_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinParadaDTO;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.paradas_service.service.dto.response.ParadaResponseDTO;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ParadasService {

    private ParadasRepository paradasRepository;
    private MonopatinClient monopatinClient;

    public List<ParadaResponseDTO> getParadas() {
        return paradasRepository.findAll()
                .stream()
                .map(this::convertToDTO)  // Method reference
                .toList();
    }

    private ParadaResponseDTO convertToDTO(Parada parada) {
        return new ParadaResponseDTO(
                parada.getId(),
                parada.getNombreParada(),
                parada.getDescripcionParada(),
                parada.getDireccionParada(),
                parada.getIdMonopatines()
        );
    }

    public ParadaResponseDTO getParadaById(Long id){
        return paradasRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public ParadaResponseDTO registrarParada(ParadaRequestDTO parada){
        return paradasRepository.save(new Parada(parada)).toDTO();
    }

    public void quitarParada(Long idParada){
        paradasRepository.deleteById(idParada);
    }

    public MonopatinParadaDTO ubicarMonopatinEnParada(Long idParada, Long idMonopatin){
        Parada parada = paradasRepository.findById(idParada).orElse(null);
        if(parada == null){ throw new RuntimeException("Parada no encontrada"); }

        // fijarme si el monopatin ya esta en otra parada
        Long idMonopatinEnParada = paradasRepository.findMonopatinEnParada(idMonopatin);

        // actualiza la idParada del monopatin
        MonopatinResponseDTO monopatinEnParada = monopatinClient.registrarMonopatinParada(idMonopatin, idParada);
        if(monopatinEnParada == null){ throw new RuntimeException("Monopatin parada no encontrada"); }

        if(parada.getIdMonopatines().contains(idMonopatinEnParada)){
            throw new RuntimeException("El monopatin ya esta en la parada");
        }

        parada.getIdMonopatines().add(idMonopatin);
        return new MonopatinParadaDTO(
                parada.getId(),
                idMonopatinEnParada,
                parada.getNombreParada()
        );
    }

    public ParadaResponseDTO updateParada(Long idParada, ParadaRequestDTO paradaRequestDTO){
        Parada parada = paradasRepository.findById(idParada).orElse(null);
        if(parada == null){ throw new RuntimeException("Parada no encontrada"); }

        parada.setDescripcionParada(paradaRequestDTO.descripcionParada());
        parada.setDireccionParada(paradaRequestDTO.direccionParada());
        parada.setNombreParada(paradaRequestDTO.nombreParada());
        parada.setIdMonopatines(paradaRequestDTO.idMonopatines());

        paradasRepository.save(parada);

        return convertToDTO(parada);
    }

    public List<MonopatinParadaDTO> getMonopatinesByParada(Long idParada){
        Parada p = paradasRepository.findById(idParada).orElse(null);
        if (p == null) throw new RuntimeException("Parada no encontrada");

        return p.getIdMonopatines().stream().map(idMonopatin -> {
            var m = monopatinClient.getMonopatinById(idMonopatin);
            if (m == null) return null;
            return new MonopatinParadaDTO(
                  p.getId(),
                  m.id(),
                  p.getNombreParada()
            );
        }).filter(Objects::nonNull).toList();
    }
}
