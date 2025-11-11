package tpe.microservicios.viajes_service.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tpe.microservicios.viajes_service.clients.AccountClient;
import tpe.microservicios.viajes_service.clients.MonopatinClient;
import tpe.microservicios.viajes_service.clients.ParadasClient;
import tpe.microservicios.viajes_service.domains.Viaje;
import tpe.microservicios.viajes_service.dto.ViajeDTO;
import tpe.microservicios.viajes_service.repository.ViajeRepository;
import tpe.microservicios.viajes_service.service.dto.request.FinalizarViajeDTO;
import tpe.microservicios.viajes_service.service.dto.request.ViajeRequestDTO;
import tpe.microservicios.viajes_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.ParadaResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.UserAccountResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.ViajeResponseDTO;
import tpe.microservicios.viajes_service.utils.EstadoViaje;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        // Validamos existencia de todo
        var account = accountClient.getAccountById(viajeRequestDTO.idUserAccount());
        if (account == null) {
            throw new RuntimeException("User account not found");
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        var monopatin = monopatinClient.getMonopatinById(viajeRequestDTO.idMonopatin());
        if (monopatin == null) {
            throw new RuntimeException("Monopatin not found");
        }

        var parada = paradasClient.getParadaById(viajeRequestDTO.idParadaOrigen());
        if (parada == null) {
            throw new RuntimeException("Parada origen not found");
        }

        Viaje v = viajeRepository.save(new Viaje(new ViajeRequestDTO(account.idAccount(), monopatin.id(), parada.id())));

        return convertToViajeResponseDTO(v);
    }

    private float calcularCostoViaje(Viaje viaje){
        LocalDateTime inicio = LocalDateTime.of(
                viaje.getFechaViaje().fechaInicioViaje(),
                viaje.getFechaViaje().horarioInicioViaje()
        );

        LocalDateTime fin = LocalDateTime.of(
                viaje.getFechaViaje().fechaFinViaje(),
                viaje.getFechaViaje().horarioFinViaje()
        );

        return tarifaService.calcularCosto(inicio, fin, viaje.getPausas().size() * 5L);
    }

    public ViajeResponseDTO pausarViaje(Long viajeId, float idAccount){
        AccountResponseDTO account = accountClient.getAccountById(viajeId);
        if (account == null) {
            throw new RuntimeException("User account not found");
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        Viaje viaje = viajeRepository.findById(viajeId).orElse(null);
        if (viaje == null) {
            throw new RuntimeException("Viaje not found");
        }

        if(viaje.getEstado() == EstadoViaje.PAUSA) throw new RuntimeException("El viaje actual esta en pausa");

        viaje.pausar();
        viajeRepository.save(viaje);

        return convertToViajeResponseDTO(viaje);
    }

    public ViajeResponseDTO reanudarViaje(Long viajeId, float idAccount){
        AccountResponseDTO account = accountClient.getAccountById(viajeId);
        if (account == null) {
            throw new RuntimeException("User account not found");
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        Viaje viaje = viajeRepository.findById(viajeId).orElse(null);
        if (viaje == null) {
            throw new RuntimeException("Viaje not found");
        }

        if(viaje.getEstado() != EstadoViaje.PAUSA) throw new RuntimeException("El viaje actual no esta en pausa");

        viaje.reanudarUltimaPausa();
        viajeRepository.save(viaje);

        return convertToViajeResponseDTO(viaje);
    }

    public ViajeResponseDTO getViajeById(Long viajeId){
       Viaje v = viajeRepository.findById(viajeId).orElse(null);

       if (v == null){
           throw new RuntimeException("Viaje not found");
       }

       return convertToViajeResponseDTO(v);
    }

    public ViajeResponseDTO finalizarViaje(Long viajeId, FinalizarViajeDTO viajeDTO) {
        AccountResponseDTO account = accountClient.getAccountById(viajeDTO.idUserAccount());
        if (account == null) {
            throw new RuntimeException("User account not found");
        }

        if (accountClient.isCuentaAnulada(account.idAccount()).cuentaAnulada()){
            throw new RuntimeException("Cuenta anulada");
        }

        ParadaResponseDTO parada = paradasClient.getParadaById(viajeDTO.idParadaDestino());
        if (parada == null) {
            throw new RuntimeException("Parada destino not found");
        }

        Viaje viaje = viajeRepository.findById(viajeId).orElse(null);
        if (viaje == null) {
            throw new RuntimeException("Viaje not found");
        }

        viaje.finalizar(viajeDTO.idParadaDestino(), viajeDTO.kilometros(), 0);
        float costo = this.calcularCostoViaje(viaje);
        viaje.setCostoViaje(costo);

        if(account.saldo() < costo){
            throw new RuntimeException("Saldo insuficiente para pagar el viaje");
        }

        accountClient.updateSaldo(viajeDTO.idUserAccount(), account.saldo() - costo);

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

    public void deleteViaje(Long idViaje){
        if(viajeRepository.findById(idViaje).isEmpty()){
            throw new RuntimeException("Viaje not found");
        }
        viajeRepository.deleteById(idViaje);
    }
    public List<ViajeDTO> getMonopatinUsado(){
        return viajeRepository.reporteKilometrosPorMonopatin();
    }
}
