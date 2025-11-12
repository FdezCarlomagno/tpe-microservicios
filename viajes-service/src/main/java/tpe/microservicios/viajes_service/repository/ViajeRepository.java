package tpe.microservicios.viajes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.viajes_service.domains.Viaje;
import tpe.microservicios.viajes_service.dto.ViajeDTO;

import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    @Query("SELECT new tpe.microservicios.viajes_service.dto.ViajeDTO(" +
            "v.idMonopatin, " +
            "CAST(SUM(v.kilometros) AS float)) " +
            "FROM Viaje v " +
            "WHERE v.estado = 'FINALIZADO' " +
            "GROUP BY v.idMonopatin " +
            "ORDER BY SUM(v.kilometros) DESC")
    List<ViajeDTO> reporteKilometrosPorMonopatin();
}
