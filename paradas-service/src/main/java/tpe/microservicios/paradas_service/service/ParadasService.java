package tpe.microservicios.paradas_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpe.microservicios.paradas_service.clients.MonopatinClient;
import tpe.microservicios.paradas_service.domain.Parada;
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
@Transactional
@RequiredArgsConstructor
public class ParadasService {

    // CORRECCIÓN: Hacer los campos final para inyección correcta con @RequiredArgsConstructor
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
            throw new IllegalArgumentException("ID de parada inválido: " + id);
        }

        return paradasRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Parada no encontrada con id: " + id));
    }

    public ParadaResponseDTO registrarParada(ParadaRequestDTO parada) {
        if (parada == null) {
            throw new IllegalArgumentException("ParadaRequestDTO no puede ser null");
        }

        // Validar datos requeridos
        if (parada.nombreParada() == null || parada.nombreParada().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la parada es obligatorio");
        }

        if (parada.direccionParada() == null || parada.direccionParada().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección de la parada es obligatoria");
        }

        Parada nuevaParada = new Parada(parada);
        return paradasRepository.save(nuevaParada).toDTO();
    }

    public void quitarParada(Long idParada) {
        if (idParada == null || idParada <= 0) {
            throw new IllegalArgumentException("ID de parada inválido: " + idParada);
        }

        // CORRECCIÓN: Verificar existencia antes de eliminar
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new IllegalArgumentException("Parada no encontrada con id: " + idParada));

        // Verificar que no tenga monopatines antes de eliminar
        if (parada.getIdMonopatines() != null && !parada.getIdMonopatines().isEmpty()) {
            throw new IllegalStateException(
                    String.format("No se puede eliminar la parada %d porque tiene %d monopatín(es) ubicado(s)",
                            idParada, parada.getIdMonopatines().size())
            );
        }

        paradasRepository.deleteById(idParada);
        log.info("Parada {} eliminada correctamente", idParada);
    }

    public MonopatinParadaDTO ubicarMonopatinEnParada(Long idParada, Long idMonopatin) {
        // Validaciones de entrada
        if (idParada == null || idParada <= 0) {
            throw new IllegalArgumentException("ID de parada inválido: " + idParada);
        }

        if (idMonopatin == null || idMonopatin <= 0) {
            throw new IllegalArgumentException("ID de monopatín inválido: " + idMonopatin);
        }

        // Buscar la parada
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new IllegalArgumentException("Parada no encontrada con id: " + idParada));

        // CORRECCIÓN: Verificar si el monopatín ya está en esta parada
        if (parada.getIdMonopatines() != null && parada.getIdMonopatines().contains(idMonopatin)) {
            throw new IllegalStateException(
                    String.format("El monopatín %d ya está ubicado en la parada %d", idMonopatin, idParada)
            );
        }

        // Verificar si el monopatín está en otra parada
        Long idParadaActual = paradasRepository.findMonopatinEnParada(idMonopatin);

        if (idParadaActual != null && !idParadaActual.equals(idParada)) {
            // Remover de la parada anterior
            Parada paradaAnterior = paradasRepository.findById(idParadaActual).orElse(null);
            if (paradaAnterior != null && paradaAnterior.getIdMonopatines() != null) {
                paradaAnterior.getIdMonopatines().remove(idMonopatin);
                paradasRepository.save(paradaAnterior);
                log.info("Monopatín {} removido de parada {}", idMonopatin, idParadaActual);
            }
        }

        // Actualizar la ubicación del monopatín en el servicio de monopatines
        MonopatinResponseDTO monopatinActualizado = monopatinClient.registrarMonopatinParada(idMonopatin, idParada);

        if (monopatinActualizado == null) {
            throw new IllegalStateException(
                    String.format("Error al registrar monopatín %d en parada %d", idMonopatin, idParada)
            );
        }

        // CORRECCIÓN: Agregar el ID del monopatín (Long) a la lista, no el DTO completo
        if (parada.getIdMonopatines() == null) {
            parada.setIdMonopatines(new ArrayList<>());
        }
        parada.getIdMonopatines().add(idMonopatin);

        // CORRECCIÓN: Guardar la parada actualizada
        paradasRepository.save(parada);

        log.info("Monopatín {} ubicado en parada {} exitosamente", idMonopatin, idParada);

        // CORRECCIÓN: Retornar el ID correcto del monopatín
        return new MonopatinParadaDTO(
                parada.getId(),
                idMonopatin,
                parada.getNombreParada()
        );
    }

    public ParadaResponseDTO updateParada(Long idParada, ParadaRequestDTO paradaRequestDTO) {
        // Validaciones de entrada
        if (idParada == null || idParada <= 0) {
            throw new IllegalArgumentException("ID de parada inválido: " + idParada);
        }

        if (paradaRequestDTO == null) {
            throw new IllegalArgumentException("ParadaRequestDTO no puede ser null");
        }

        // Buscar la parada
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new IllegalArgumentException("Parada no encontrada con id: " + idParada));

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
            throw new IllegalArgumentException("ID de parada inválido: " + idParada);
        }

        // Buscar la parada
        Parada parada = paradasRepository.findById(idParada)
                .orElseThrow(() -> new IllegalArgumentException("Parada no encontrada con id: " + idParada));

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