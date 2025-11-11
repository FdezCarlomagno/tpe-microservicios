package tpe.microservicios.viajes_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tpe.microservicios.viajes_service.clients.AccountClient;
import tpe.microservicios.viajes_service.clients.MonopatinClient;
import tpe.microservicios.viajes_service.clients.ParadasClient;
import tpe.microservicios.viajes_service.domains.Viaje;
import tpe.microservicios.viajes_service.repository.ViajeRepository;
import tpe.microservicios.viajes_service.service.dto.request.FinalizarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.ViajeRequestDTO;
import tpe.microservicios.viajes_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.ParadaResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.ViajeResponseDTO;
import tpe.microservicios.viajes_service.utils.EstadoViaje;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ViajeService {

    private final AccountClient accountClient;
    private final MonopatinClient monopatinClient;
    private final ParadasClient paradasClient;
    private final ViajeRepository viajeRepository;
    private final TarifaService tarifaService;

    public ViajeResponseDTO addViaje(ViajeRequestDTO viajeRequestDTO) {
        // Validamos existencia y estado de la cuenta
        var account = accountClient.getAccountById(viajeRequestDTO.idUserAccount());
        if (account == null) {
            throw new IllegalArgumentException("User account not found with id: " + viajeRequestDTO.idUserAccount());
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()) {
            throw new IllegalStateException("Cuenta anulada, no puede iniciar viajes");
        }

        // Validamos existencia del monopatín
        var monopatin = monopatinClient.getMonopatinById(viajeRequestDTO.idMonopatin());
        if (monopatin == null) {
            throw new IllegalArgumentException("Monopatin not found with id: " + viajeRequestDTO.idMonopatin());
        }

        // TODO: Agregar validación de disponibilidad del monopatín
        // if (!monopatin.isDisponible()) {
        //     throw new IllegalStateException("Monopatin no disponible");
        // }

        // Validamos existencia de la parada de origen
        var parada = paradasClient.getParadaById(viajeRequestDTO.idParadaOrigen());
        if (parada == null) {
            throw new IllegalArgumentException("Parada origen not found with id: " + viajeRequestDTO.idParadaOrigen());
        }

        Viaje v = viajeRepository.save(new Viaje(new ViajeRequestDTO(
                account.idAccount(),
                monopatin.id(),
                parada.id()
        )));

        return convertToViajeResponseDTO(v);
    }

    private float calcularCostoViaje(Viaje viaje) {
        LocalDateTime inicio = LocalDateTime.of(
                viaje.getFechaViaje().fechaInicioViaje(),
                viaje.getFechaViaje().horarioInicioViaje()
        );

        LocalDateTime fin = LocalDateTime.of(
                viaje.getFechaViaje().fechaFinViaje(),
                viaje.getFechaViaje().horarioFinViaje()
        );

        // Calculamos tiempo total de pausas (asumiendo 5 minutos por pausa)
        long minutosEnPausa = viaje.getPausas().size() * 5L;

        return tarifaService.calcularCosto(inicio, fin, minutosEnPausa);
    }

    public ViajeResponseDTO pausarViaje(Long viajeId) {
        // Buscamos el viaje
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje not found with id: " + viajeId));

        // Validamos la cuenta del usuario
        AccountResponseDTO account = accountClient.getAccountById(viaje.getIdUserAccount());
        if (account == null) {
            throw new IllegalStateException("User account not found for viaje id: " + viajeId);
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()) {
            throw new IllegalStateException("Cuenta anulada, no puede pausar el viaje");
        }

        // Validamos el estado del viaje
        if (viaje.getEstado() == EstadoViaje.PAUSA) {
            throw new IllegalStateException("El viaje ya está en pausa");
        }

        if (viaje.getEstado() == EstadoViaje.FINALIZADO) {
            throw new IllegalStateException("No se puede pausar un viaje finalizado");
        }

        viaje.pausar();
        viajeRepository.save(viaje);

        return convertToViajeResponseDTO(viaje);
    }

    public ViajeResponseDTO reanudarViaje(Long viajeId) {
        // Buscamos el viaje
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje not found with id: " + viajeId));

        // Validamos la cuenta del usuario
        AccountResponseDTO account = accountClient.getAccountById(viaje.getIdUserAccount());
        if (account == null) {
            throw new IllegalStateException("User account not found for viaje id: " + viajeId);
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()) {
            throw new IllegalStateException("Cuenta anulada, no puede reanudar el viaje");
        }

        // Validamos el estado del viaje
        if (viaje.getEstado() != EstadoViaje.PAUSA) {
            throw new IllegalStateException("El viaje no está en pausa, estado actual: " + viaje.getEstado());
        }

        viaje.reanudarUltimaPausa();
        viajeRepository.save(viaje);

        return convertToViajeResponseDTO(viaje);
    }

    public ViajeResponseDTO getViajeById(Long viajeId) {
        Viaje v = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje not found with id: " + viajeId));

        return convertToViajeResponseDTO(v);
    }

    public ViajeResponseDTO finalizarViaje(Long viajeId, FinalizarViajeDTO viajeDTO) {
        // Buscamos el viaje
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje not found with id: " + viajeId));

        // Validamos el estado del viaje
        if (viaje.getEstado() == EstadoViaje.FINALIZADO) {
            throw new IllegalStateException("El viaje ya está finalizado");
        }

        // Si está en pausa, primero cerramos la pausa
        if (viaje.getEstado() == EstadoViaje.PAUSA) {
            viaje.reanudarUltimaPausa();
        }

        // Validamos la cuenta del usuario (usamos el ID del viaje, no del DTO)
        AccountResponseDTO account = accountClient.getAccountById(viaje.getIdUserAccount());
        if (account == null) {
            throw new IllegalStateException("User account not found for viaje id: " + viajeId);
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()) {
            throw new IllegalStateException("Cuenta anulada, no puede finalizar el viaje");
        }

        // Validamos la parada de destino
        ParadaResponseDTO parada = paradasClient.getParadaById(viajeDTO.idParadaDestino());
        if (parada == null) {
            throw new IllegalArgumentException("Parada destino not found with id: " + viajeDTO.idParadaDestino());
        }

        // Validamos que los kilómetros sean positivos
        if (viajeDTO.kilometros() <= 0) {
            throw new IllegalArgumentException("Los kilómetros deben ser mayores a 0");
        }

        // Finalizamos el viaje
        viaje.finalizar(viajeDTO.idParadaDestino(), viajeDTO.kilometros(), 0);

        // Calculamos el costo
        float costo = this.calcularCostoViaje(viaje);
        viaje.setCostoViaje(costo);

        // Validamos saldo suficiente
        if (account.saldo() < costo) {
            // Revertimos el estado si no hay saldo
            throw new IllegalStateException(
                    String.format("Saldo insuficiente. Costo: %.2f, Saldo disponible: %.2f",
                            costo, account.saldo())
            );
        }

        // Actualizamos el saldo (CORRECCIÓN: usar idUserAccount del viaje, no del DTO)
        float nuevoSaldo = account.saldo() - costo;
        accountClient.updateSaldo(viaje.getIdUserAccount(), nuevoSaldo);

        // Guardamos el viaje finalizado
        viajeRepository.save(viaje);

        return convertToViajeResponseDTO(viaje);
    }

    private ViajeResponseDTO convertToViajeResponseDTO(Viaje viaje) {
        return new ViajeResponseDTO(
                viaje.getId(),
                viaje.getIdMonopatin(),
                viaje.getIdUserAccount(),
                viaje.getIdParadaOrigen(),
                viaje.getEstado(),
                viaje.getFechaViaje().fechaInicioViaje(),
                viaje.getFechaViaje().horarioInicioViaje()
        );
    }

    public void deleteViaje(Long idViaje) {
        if (!viajeRepository.existsById(idViaje)) {
            throw new IllegalArgumentException("Viaje not found with id: " + idViaje);
        }
        viajeRepository.deleteById(idViaje);
    }
}