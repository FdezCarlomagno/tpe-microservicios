package tpe.microservicios.viajes_service.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarifas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tipo; // "NORMAL", "PAUSA_EXCEDIDA", "REANUDADA"

    @Column(nullable = false)
    private Float valor;

    @Column(name = "pausa_max_minutos")
    private Integer pausaMaxMinutos; // solo se usa para configuración general

}
