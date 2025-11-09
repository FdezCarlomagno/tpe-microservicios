package tpe.microservicios.monopatin_service.domain;

import jakarta.persistence.*;
import lombok.*;
import tpe.microservicios.monopatin_service.service.dto.request.MonopatinRequestDTO;
import tpe.microservicios.monopatin_service.utils.EstadoMonopatin;

@Entity
@Table(name = "monopatines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Monopatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMonopatin estado;

    @Column
    private float km;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(name = "id_parada")
    private Long idParada;

    // Constructor para crear desde DTO
    public Monopatin(MonopatinRequestDTO dto) {
        this.estado = dto.estado();
        this.idParada = dto.idParada();
        initDisponibilidad();
    }

    // Constructor con estado (útil para tests)
    public Monopatin(EstadoMonopatin estado) {
        this.estado = estado;
        initDisponibilidad();
    }

    // Método que se ejecuta después de cargar la entidad
    @PostLoad
    @PostPersist
    private void syncDisponibilidad() {
        initDisponibilidad();
    }

    private void initDisponibilidad() {
        this.disponible = switch (estado) {
            case ACTIVADO -> true;
            case DESACTIVADO, MANTENIMIENTO -> false;
        };
    }

    // Métodos de negocio
    public void activar() {
        this.estado = EstadoMonopatin.ACTIVADO;
        this.disponible = true;
    }

    public void desactivar() {
        this.estado = EstadoMonopatin.DESACTIVADO;
        this.disponible = false;
    }

    public void enviarAMantenimiento() {
        this.estado = EstadoMonopatin.MANTENIMIENTO;
        this.disponible = false;
    }

    public boolean puedeSerAlquilado() {
        return this.disponible && this.estado == EstadoMonopatin.ACTIVADO;
    }
}