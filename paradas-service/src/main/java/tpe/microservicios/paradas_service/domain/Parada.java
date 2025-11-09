package tpe.microservicios.paradas_service.domain;


import jakarta.persistence.*;
import lombok.*;
import tpe.microservicios.paradas_service.service.dto.request.ParadaRequestDTO;
import tpe.microservicios.paradas_service.service.dto.response.ParadaResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Parada {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombreParada;

    @Column
    private String descripcionParada;

    @Column
    private String direccionParada;

    /**
     * En una parada pueden haber muchos monopatines?
     * */
    @Column
    private List<Long> idMonopatines = new ArrayList<>();

    public Parada(ParadaRequestDTO parada) {
        this.id = parada.id();
        this.descripcionParada = parada.descripcionParada();
        this.nombreParada = parada.nombreParada();
        this.direccionParada = parada.direccionParada();
        this.idMonopatines = parada.idMonopatines();
    }

    public ParadaResponseDTO toDTO(){
        return new ParadaResponseDTO(
                this.nombreParada,
                this.descripcionParada,
                this.direccionParada,
                new ArrayList<>(this.idMonopatines)
        );
    }

    public ArrayList<Long> getIdMonopatines(){
        return new ArrayList<>(this.idMonopatines);
    }


}
