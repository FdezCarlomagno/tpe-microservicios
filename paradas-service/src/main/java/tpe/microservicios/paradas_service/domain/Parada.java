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
    @ElementCollection
    @CollectionTable(
            name = "parada_monopatines",
            joinColumns = @JoinColumn(name = "parada_id")
    )
    @Column(name = "monopatin_id")
    private List<Long> idMonopatines = new ArrayList<>();

    public Parada(ParadaRequestDTO parada) {
        this.descripcionParada = parada.descripcionParada();
        this.nombreParada = parada.nombreParada();
        this.direccionParada = parada.direccionParada();
    }


    public ParadaResponseDTO toDTO(){
        return new ParadaResponseDTO(
                this.id,
                this.nombreParada,
                this.descripcionParada,
                this.direccionParada,
                new ArrayList<>(this.idMonopatines)
        );
    }

    /**
     * ESTO ROMPIA TODO
     * */
    /*
    public ArrayList<Long> getIdMonopatines(){
        return new ArrayList<>(this.idMonopatines);
    }*/

}
