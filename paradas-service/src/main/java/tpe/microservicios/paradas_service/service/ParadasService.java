package tpe.microservicios.paradas_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpe.microservicios.paradas_service.clients.MonopatinClient;
import tpe.microservicios.paradas_service.domain.Parada;
import tpe.microservicios.paradas_service.exceptions.BadRequestException;
import tpe.microservicios.paradas_service.exceptions.InternalServerErrorException;
import tpe.microservicios.paradas_service.exceptions.NotFoundException;
import tpe.microservicios.paradas_service.repository.ParadasRepository;
import tpe.microservicios.paradas_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinParadaDTO;
import tpe.microservicios.paradas_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.paradas_service.service.dto.response.ParadaResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(dontRollbackOn = NotFoundException.class)
@RequiredArgsConstructor
public class ParadasService {

    private final ParadasRepository paradasRepository;
    private final MonopatinClient monopatinClient;

    public List<ParadaResponseDTO> getParadas() {
        return paradasRepository.findAll()
                .stream()
                .map(this::convertToDTO)
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

    public ParadaResponseDTO getParadaById(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("ID de parada inválido: " + id);
        }

        return paradasRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + id));
    }

    public ParadaResponseDTO registrarParada(ParadaRequestDTO parada) {
        if (parada == null) {
            throw new BadRequestException("ParadaRequestDTO no puede ser null");
        }

        // Validar datos requeridos
        if (parada.nombreParada() == null || parada.nombreParada().trim().isEmpty()) {
            throw new BadRequestException("El nombre de la parada es obligatorio");
        }

        if (parada.direccionParada() == null || parada.direccionParada().trim().isEmpty()) {
            throw new BadRequestException("La dirección de la parada es obligatoria");
        }

        Parada nuevaParada = new Parada(parada);
        return paradasRepository.save(nuevaParada).toDTO();
    }

    public void quitarParada(Long idParada) {
        if (idParada == null || idParada <= 0) {
            throw new BadRequestException("ID de parada inválido: " + idParada);
        }

        // CORRECCIÓN: Verificar existencia antes de eliminar
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + idParada));

        // Verificar que no tenga monopatines antes de eliminar
        if (parada.getIdMonopatines() != null && !parada.getIdMonopatines().isEmpty()) {
            throw new BadRequestException(
                    String.format("No se puede eliminar la parada %d porque tiene %d monopatín(es) ubicado(s)",
                            idParada, parada.getIdMonopatines().size())
            );
        }

        paradasRepository.deleteById(idParada);
        log.info("Parada {} eliminada correctamente", idParada);
    }

    public MonopatinParadaDTO ubicarMonopatinEnParada(Long idParada, Long idMonopatin) {

        if (idParada == null || idParada <= 0)
            throw new BadRequestException("ID de parada inválido: " + idParada);

        if (idMonopatin == null || idMonopatin <= 0)
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);

        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + idParada));

        if (parada.getIdMonopatines() != null && parada.getIdMonopatines().contains(idMonopatin))
            throw new BadRequestException(String.format("El monopatín %d ya está en la parada %d",
                    idMonopatin, idParada));

        // Si estaba en otra parada → removerlo
        Long idParadaActual = paradasRepository.findMonopatinEnParada(idMonopatin);

        if (idParadaActual != null && !idParadaActual.equals(idParada)) {
            Parada anterior = paradasRepository.findById(idParadaActual).orElse(null);
            if (anterior != null && anterior.getIdMonopatines() != null) {
                anterior.getIdMonopatines().remove(idMonopatin);
                paradasRepository.save(anterior);
            }
        }

        // Actualizar en MS Monopatines
        MonopatinResponseDTO actualizado = monopatinClient.registrarMonopatinParada(idMonopatin, idParada);

        if (actualizado == null)
            throw new InternalServerErrorException("No se pudo sincronizar la parada en MS Monopatines");

        parada.getIdMonopatines().add(idMonopatin);
        paradasRepository.save(parada);

        return new MonopatinParadaDTO(parada.getId(), idMonopatin, parada.getNombreParada());
    }


    public MonopatinParadaDTO removerMonopatinDeParada(Long idParada, Long idMonopatin) {

        if (idParada == null || idParada <= 0)
            throw new BadRequestException("ID de parada inválido: " + idParada);

        if (idMonopatin == null || idMonopatin <= 0)
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);

        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + idParada));

        if (parada.getIdMonopatines() == null || !parada.getIdMonopatines().contains(idMonopatin))
            throw new BadRequestException(String.format("El monopatín %d no está en la parada %d",
                    idMonopatin, idParada));

        parada.getIdMonopatines().remove(idMonopatin);
        paradasRepository.save(parada);

        // Sincronizar con el MS monopatines
        monopatinClient.removerParada(idMonopatin);

        return new MonopatinParadaDTO(parada.getId(), idMonopatin, parada.getNombreParada());
    }

    public ParadaResponseDTO updateParada(Long idParada, ParadaRequestDTO paradaRequestDTO) {
        // Validaciones de entrada
        if (idParada == null || idParada <= 0) {
            throw new BadRequestException("ID de parada inválido: " + idParada);
        }

        if (paradaRequestDTO == null) {
            throw new BadRequestException("ParadaRequestDTO no puede ser null");
        }

        // Buscar la parada
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + idParada));

        // CORRECCIÓN: Validar datos antes de actualizar
        if (paradaRequestDTO.nombreParada() != null && !paradaRequestDTO.nombreParada().trim().isEmpty()) {
            parada.setNombreParada(paradaRequestDTO.nombreParada());
        }

        if (paradaRequestDTO.descripcionParada() != null) {
            parada.setDescripcionParada(paradaRequestDTO.descripcionParada());
        }

        if (paradaRequestDTO.direccionParada() != null && !paradaRequestDTO.direccionParada().trim().isEmpty()) {
            parada.setDireccionParada(paradaRequestDTO.direccionParada());
        }

        // Actualizar monopatines solo si se proporciona la lista
        if (paradaRequestDTO.idMonopatines() != null) {
            parada.setIdMonopatines(paradaRequestDTO.idMonopatines());
        }

        Parada paradaActualizada = paradasRepository.save(parada);
        log.info("Parada {} actualizada correctamente", idParada);

        return convertToDTO(paradaActualizada);
    }

    public List<MonopatinParadaDTO> getMonopatinesByParada(Long idParada) {
        // Validación de entrada
        if (idParada == null || idParada <= 0) {
            throw new BadRequestException("ID de parada inválido: " + idParada);
        }

        // Buscar la parada
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new NotFoundException("Parada no encontrada con id: " + idParada));

        // Si no hay monopatines, retornar lista vacía
        if (parada.getIdMonopatines() == null || parada.getIdMonopatines().isEmpty()) {
            log.info("La parada {} no tiene monopatines", idParada);
            return List.of();
        }

        // Mapear monopatines
        return parada.getIdMonopatines().stream().map(idMonopatin -> {
                    try {
                        MonopatinResponseDTO monopatin = monopatinClient.getMonopatinById(idMonopatin);

                        if (monopatin == null) {
                            log.warn("Monopatín {} no encontrado en el servicio de monopatines pero está registrado en parada {}",
                                    idMonopatin, idParada);
                            return null;
                        }

                        return new MonopatinParadaDTO(
                                parada.getId(),
                                monopatin.id(),
                                parada.getNombreParada()
                        );
                    } catch (Exception e) {
                        log.error("Error al obtener monopatín {} para parada {}: {}",
                                idMonopatin, idParada, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}