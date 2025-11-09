package tpe.microservicios.viajes_service.domains;

import jakarta.persistence.*;
import lombok.*;
import tpe.microservicios.viajes_service.service.TarifaService;
import tpe.microservicios.viajes_service.service.dto.request.ViajeRequestDTO;
import tpe.microservicios.viajes_service.utils.EstadoViaje;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FechaViaje fechaViaje;

    @ElementCollection
    @CollectionTable(name = "pausas_viaje", joinColumns = @JoinColumn(name = "viaje_id"))
    private List<FechaViaje> pausas = new ArrayList<>();

    private Long idMonopatin;
    private Long idUserAccount;
    private Long idParadaOrigen;
    private Long idParadaDestino;

    private float kilometros;
    private float costoViaje;

    @Enumerated(EnumType.STRING)
    private EstadoViaje estado;


    public Viaje(){
        this.iniciar();
    }

    public Viaje(ViajeRequestDTO viaje){
        //fecha de viaje de inicio
        this.iniciar();
        this.idMonopatin = viaje.idMonopatin();
        this.idUserAccount = viaje.idUserAccount();
        this.idParadaOrigen = viaje.idParadaOrigen();
        this.kilometros = 0;
        this.costoViaje = 0;
    }



    // ======= Métodos de negocio =======

    public void iniciar() {
        this.fechaViaje = new FechaViaje(LocalDate.now(), LocalTime.now(), null, null);
        this.estado = EstadoViaje.INICIADO;
    }

    public void pausar() {
        if (this.estado == EstadoViaje.INICIADO) {
            pausas.add(new FechaViaje(LocalDate.now(), LocalTime.now(), null, null));
            this.estado = EstadoViaje.PAUSA;
        }
    }

    public void reanudarUltimaPausa() {
        if (this.estado == EstadoViaje.PAUSA && !this.pausas.isEmpty()) {
            FechaViaje ultima = this.pausas.get(this.pausas.size() - 1);
            this.pausas.set(
                    this.pausas.size() - 1,
                    new FechaViaje(ultima.fechaInicioViaje(), ultima.horarioInicioViaje(), LocalDate.now(), LocalTime.now())
            );
            this.estado = EstadoViaje.INICIADO;
        }
    }

    public void finalizar(Long idParadaDestino, float kilometros, float costoViaje) {
        this.idParadaDestino = idParadaDestino;
        this.fechaViaje = new FechaViaje(
                this.fechaViaje.fechaInicioViaje(),
                this.fechaViaje.horarioInicioViaje(),
                LocalDate.now(),
                LocalTime.now()
        );
        this.kilometros = kilometros;
        this.costoViaje = costoViaje;
        this.estado = EstadoViaje.FINALIZADO;
    }

    public boolean puedePagar(float saldo) {
        return saldo >= this.costoViaje;
    }
}

