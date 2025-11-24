package tpe.microservicios.monopatin_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tpe.microservicios.monopatin_service.clients.ParadaClient;
import tpe.microservicios.monopatin_service.domain.Monopatin;
import tpe.microservicios.monopatin_service.exceptions.BadRequestException;
import tpe.microservicios.monopatin_service.exceptions.ConflictException;
import tpe.microservicios.monopatin_service.exceptions.NotFoundException;
import tpe.microservicios.monopatin_service.repository.MonopatinRepository;
import tpe.microservicios.monopatin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.monopatin_service.service.dto.response.ParadaResponseDTO;
import tpe.microservicios.monopatin_service.utils.EstadoMonopatin;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MonopatinService {

    private final MonopatinRepository monopatinRepository;
    private final ParadaClient paradaClient;

    public List<MonopatinResponseDTO> findAll() {
        return monopatinRepository.findAll().stream().map(monopatin -> {
            try {
                ParadaResponseDTO parada = paradaClient.getParadaById(monopatin.getIdParada());
                String nombreParada = (parada != null) ? parada.nombreParada() : "Parada desconocida";

                return new MonopatinResponseDTO(
                        monopatin.getId(),
                        nombreParada,
                        monopatin.getDisponible(),
                        monopatin.getEstado()
                );
            } catch (Exception e) {
                log.error("Error al obtener parada {} para monopatín {}: {}",
                        monopatin.getIdParada(), monopatin.getId(), e.getMessage());
                // Retornar monopatín con nombre de parada por defecto
                return new MonopatinResponseDTO(
                        monopatin.getId(),
                        "Parada no disponible",
                        monopatin.getDisponible(),
                        monopatin.getEstado()
                );
            }
        }).toList();
    }

    public MonopatinResponseDTO findById(Long id) {
        // Validación de entrada
        if (id == null || id <= 0) {
            throw new NotFoundException("ID de monopatín inválido: " + id);
        }

        // CORRECCIÓN: Usar orElseThrow en lugar de orElse(null)
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + id));

        // CORRECCIÓN: Validar respuesta del cliente
        String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());

        return new MonopatinResponseDTO(
                monopatin.getId(),
                nombreParada,
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public MonopatinResponseDTO registrarMonopatinEnMantenimiento(Long id) {
        // Validación de entrada
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de monopatín inválido: " + id);
        }

        // CORRECCIÓN: Usar orElseThrow y retornar DTO
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + id));

        // CORRECCIÓN: Validar y lanzar excepción si ya está en mantenimiento
        if (monopatin.getEstado() == EstadoMonopatin.MANTENIMIENTO) {
            throw new BadRequestException(
                    String.format("El monopatín %d ya está en mantenimiento", id)
            );
        }

        monopatin.enviarAMantenimiento();
        monopatinRepository.save(monopatin);

        log.info("Monopatín {} enviado a mantenimiento", id);

        String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());
        return new MonopatinResponseDTO(
                monopatin.getId(),
                nombreParada,
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }
    public MonopatinResponseDTO agregarMonopatin(MonopatinRequestDTO dto) {

        if (dto == null) {
            throw new BadRequestException("MonopatinRequestDTO no puede ser null");
        }

        Long paradaSolicitada = dto.idParada();

        // Paso 1: crear el monopatín SIN parada
        Monopatin nuevo = new Monopatin(dto);
        nuevo.setIdParada(null); // ← clave: no asignamos parada todavía
        Monopatin guardado = monopatinRepository.save(nuevo);

        // Paso 2: si vino una parada, validarlo llamando al microservicio
        if (paradaSolicitada != null) {
            try {
                // validar existencia de la parada
                ParadaResponseDTO parada = paradaClient.getParadaById(paradaSolicitada);
                if (parada == null) {
                    throw new NotFoundException("Parada no encontrada con id " + paradaSolicitada);
                }

                // notificar al MS de paradas
                paradaClient.ubicarMonopatinEnParada(paradaSolicitada, guardado.getId());

                // si llegó hasta acá → asignación exitosa → actualizar en BD
                guardado.setIdParada(paradaSolicitada);
                monopatinRepository.save(guardado);

            } catch (Exception e) {
                log.error("No se pudo asignar monopatín {} a la parada {}: {}",
                        guardado.getId(), paradaSolicitada, e.getMessage());

                // ⚠ Acá NO hacemos rollback global
                // porque el monopatín fue creado correctamente
                // solo informamos que quedó SIN parada.
            }
        }

        return new MonopatinResponseDTO(
                guardado.getId(),
                obtenerNombreParadaSeguro(guardado.getIdParada()),
                guardado.getDisponible(),
                guardado.getEstado()
        );
    }


    public void quitarMonopatin(Long id) {
        // Validación de entrada
        if (id == null || id <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + id);
        }

        // CORRECCIÓN: Verificar existencia antes de eliminar
        Monopatin monopatin = monopatinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + id));

        // Validar que no esté en uso (disponible = false significa en uso)
        if (!monopatin.getDisponible()) {
            log.warn("Intentando eliminar monopatín {} que está en uso", id);
            throw new ConflictException("Intentando eliminar monopatin que esta en uso");
        }

        if (monopatin.getIdParada() != null) {
            throw new ConflictException("Intentando eliminar monopatin que esta asignado a una parada");
        }

        monopatinRepository.deleteById(id);
        log.info("Monopatín {} eliminado correctamente", id);
    }

    public MonopatinResponseDTO actualizarParada(Long paradaId, Long idMonopatin) {
        // Validaciones de entrada
        if (paradaId == null || paradaId <= 0) {
            throw new BadRequestException("ID de parada inválido: " + paradaId);
        }

        if (idMonopatin == null || idMonopatin <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);
        }

        // CORRECCIÓN: Validar existencia de parada y lanzar excepción si no existe
        ParadaResponseDTO parada = paradaClient.getParadaById(paradaId);
        if (parada == null) {
            throw new NotFoundException("Parada no encontrada con id: " + paradaId);
        }

        // CORRECCIÓN: Usar orElseThrow
        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + idMonopatin));

        // Validar que el monopatín esté disponible para cambiar de parada
        if (!monopatin.getDisponible()) {
            throw new BadRequestException(
                    String.format("El monopatín %d no está disponible para cambiar de parada", idMonopatin)
            );
        }
        /*
        * Sincronizar con el paradaClient
        * */
        monopatin.setIdParada(paradaId);
        monopatinRepository.save(monopatin);

        log.info("Monopatín {} movido a parada {}", idMonopatin, paradaId);

        return new MonopatinResponseDTO(
                monopatin.getId(),
                parada.nombreParada(),
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public MonopatinResponseDTO actualizarMonopatin(Long idMonopatin, MonopatinRequestDTO newMonopatin) {
        // Validaciones de entrada
        if (idMonopatin == null || idMonopatin <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);
        }

        if (newMonopatin == null) {
            throw new BadRequestException("MonopatinRequestDTO no puede ser null");
        }

        // CORRECCIÓN: Usar orElseThrow
        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + idMonopatin));

        // CORRECCIÓN: Validar parada si se está actualizando
        if (newMonopatin.idParada() != null) {
            validarParadaExiste(newMonopatin.idParada());
            monopatin.setIdParada(newMonopatin.idParada());
        }

        // CORRECCIÓN: Validar estado válido
        if (newMonopatin.estado() != null) {
            monopatin.setEstado(newMonopatin.estado());
        }

        // CORRECCIÓN: Validar kilómetros no negativos
        if (newMonopatin.km() < 0) {
            throw new BadRequestException("Los kilómetros no pueden ser negativos");
        }
        monopatin.setKm(newMonopatin.km());

        // Actualizar disponibilidad
        monopatin.setDisponible(newMonopatin.disponible());

        monopatinRepository.save(monopatin);
        log.info("Monopatín {} actualizado correctamente", idMonopatin);

        String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());

        return new MonopatinResponseDTO(
                monopatin.getId(),
                nombreParada,
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public MonopatinResponseDTO removerDeParada(Long idMonopatin) {

        if (idMonopatin == null || idMonopatin <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);
        }

        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + idMonopatin));

        monopatin.setIdParada(null);
        monopatinRepository.save(monopatin);

        return new MonopatinResponseDTO(
                monopatin.getId(),
                "Sin parada asignada",
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public List<MonopatinResponseDTO> findMonopatinesByParada(Long idParada) {
        var parada = paradaClient.getParadaById(idParada);
        if (parada == null) {
            throw new NotFoundException("Parada no encontrada: " + idParada);
        }

        List<Monopatin> monopatines = monopatinRepository.findMonopatinesByParada(idParada);

        return monopatines.stream()
                .map(monopatin -> {
                    String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());
                    return new MonopatinResponseDTO(
                            monopatin.getId(),
                            nombreParada,
                            monopatin.getDisponible(),
                            monopatin.getEstado()
                    );
                })
                .toList();
    }

    public MonopatinResponseDTO activarMonopatin(Long idMonopatin) {
        // Validación de entrada
        if (idMonopatin == null || idMonopatin <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);
        }

        // CORRECCIÓN: Usar orElseThrow
        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + idMonopatin));

        // CORRECCIÓN: Validar que no esté en mantenimiento
        if (monopatin.getEstado() == EstadoMonopatin.MANTENIMIENTO) {
            throw new BadRequestException(
                    String.format("No se puede activar el monopatín %d porque está en mantenimiento", idMonopatin)
            );
        }

        monopatin.activar();
        monopatinRepository.save(monopatin);

        log.info("Monopatín {} activado", idMonopatin);

        String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());

        return new MonopatinResponseDTO(
                monopatin.getId(),
                nombreParada,
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public MonopatinResponseDTO desactivarMonopatin(Long idMonopatin) {
        // Validación de entrada
        if (idMonopatin == null || idMonopatin <= 0) {
            throw new BadRequestException("ID de monopatín inválido: " + idMonopatin);
        }

        // CORRECCIÓN: Usar orElseThrow
        Monopatin monopatin = monopatinRepository.findById(idMonopatin)
                .orElseThrow(() -> new NotFoundException("Monopatín no encontrado con id: " + idMonopatin));

        monopatin.desactivar();
        monopatinRepository.save(monopatin);

        log.info("Monopatín {} desactivado", idMonopatin);

        String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());

        return new MonopatinResponseDTO(
                monopatin.getId(),
                nombreParada,
                monopatin.getDisponible(),
                monopatin.getEstado()
        );
    }

    public List<MonopatinResponseDTO> findAllDisponibles() {
        List<Monopatin> monopatines = monopatinRepository.findAllDisponibles();

        // CORRECCIÓN: Usar streams como en otros métodos
        return monopatines.stream()
                .map(monopatin -> {
                    String nombreParada = obtenerNombreParadaSeguro(monopatin.getIdParada());
                    return new MonopatinResponseDTO(
                            monopatin.getId(),
                            nombreParada,
                            monopatin.getDisponible(),
                            monopatin.getEstado()
                    );
                })
                .toList();
    }

    public List<MonopatinResponseDTO> findByParada(Long idParada) {
        // Validación de entrada
        if (idParada == null || idParada <= 0) {
            throw new BadRequestException("ID de parada inválido: " + idParada);
        }

        // CORRECCIÓN: Validar existencia de parada
        ParadaResponseDTO parada = paradaClient.getParadaById(idParada);
        if (parada == null) {
            throw new NotFoundException("Parada no encontrada con id: " + idParada);
        }

        // Verificar si hay monopatines en la parada
        if (parada.idMonopatines() == null || parada.idMonopatines().isEmpty()) {
            log.info("La parada {} no tiene monopatines", idParada);
            return List.of();
        }

        return parada.idMonopatines().stream()
                .map(idMonopatin -> {
                    try {
                        Monopatin monopatin = monopatinRepository.findById(idMonopatin).orElse(null);
                        if (monopatin == null) {
                            log.warn("Monopatín {} registrado en parada {} pero no existe en BD",
                                    idMonopatin, idParada);
                            return null;
                        }
                        return new MonopatinResponseDTO(
                                monopatin.getId(),
                                parada.nombreParada(),
                                monopatin.getDisponible(),
                                monopatin.getEstado()
                        );
                    } catch (Exception e) {
                        log.error("Error al procesar monopatín {}: {}", idMonopatin, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    //Método auxiliar para obtener nombre de parada de forma segura
    private String obtenerNombreParadaSeguro(Long idParada) {
        if (idParada == null) {
            return "Sin parada asignada";
        }

        try {
            ParadaResponseDTO parada = paradaClient.getParadaById(idParada);
            return (parada != null && parada.nombreParada() != null)
                    ? parada.nombreParada()
                    : "Parada desconocida";
        } catch (Exception e) {
            log.error("Error al obtener parada {}: {}", idParada, e.getMessage());
            return "Parada no disponible";
        }
    }

    //Método auxiliar para validar existencia de parada
    private void validarParadaExiste(Long idParada) {
        try {
            ParadaResponseDTO parada = paradaClient.getParadaById(idParada);
            if (parada == null) {
                throw new NotFoundException("Parada no encontrada con id: " + idParada);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al validar parada {}: {}", idParada, e.getMessage());
            throw new IllegalStateException(
                    String.format("No se pudo validar la existencia de la parada %d: %s",
                            idParada, e.getMessage())
            );
        }
    }
}