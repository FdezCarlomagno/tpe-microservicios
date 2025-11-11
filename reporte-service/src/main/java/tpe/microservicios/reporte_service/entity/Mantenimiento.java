package tpe.microservicios.reporte_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String estado;
    @Column
    private Date fechaMantenimiento;
    @Column
    private String observaciones;
}
