package tpe.microservicios.viajes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.viajes_service.domains.Viaje;
import tpe.microservicios.viajes_service.dto.ViajeDTO;
import tpe.microservicios.viajes_service.service.dto.response.KmRecorridosDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    @Query("SELECT new tpe.microservicios.viajes_service.dto.ViajeDTO(" +
            "v.idMonopatin, " +
            "CAST(SUM(v.kilometros) AS float)) " +
            "FROM Viaje v " +
            "WHERE v.estado = 'FINALIZADO' " +
            "GROUP BY v.idMonopatin " +
            "ORDER BY SUM(v.kilometros) DESC")
    List<ViajeDTO> reporteKilometrosPorMonopatin();

    // Consulta los monopatines con más de X viajes en un cierto año
    @Query("""
        SELECT v.idMonopatin
        FROM Viaje v
        WHERE YEAR(v.fechaViaje.fechaInicioViaje) = :anio
        GROUP BY v.idMonopatin
        HAVING COUNT(v.id) > :minViajes
    """)
    List<Long> findMonopatinesConMasDeXViajesEnAnio(int anio, long minViajes);

    @Query("""
    SELECT SUM(v.costoViaje)
    FROM Viaje v
    WHERE YEAR(v.fechaViaje.fechaFinViaje) = :anio
      AND MONTH(v.fechaViaje.fechaFinViaje) BETWEEN :mesInicio AND :mesFin
      AND v.estado = 'FINALIZADO'
""")
    Float getTotalFacturadoEnRangoMeses(int anio, int mesInicio, int mesFin);

    @Query("""
    SELECT v.idUserAccount, COUNT(v) as totalViajes
    FROM Viaje v
    WHERE v.fechaViaje.fechaInicioViaje BETWEEN :fechaInicio AND :fechaFin
      AND v.estado = 'FINALIZADO'
    GROUP BY v.idUserAccount
    ORDER BY totalViajes DESC
""")
    List<Object[]> getUsuariosMasActivos(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("""
    SELECT SUM(v.kilometros)
    FROM Viaje v
    WHERE v.idUserAccount IN :idsUsuarios
      AND v.fechaViaje.fechaInicioViaje BETWEEN :fechaInicio AND :fechaFin
      AND v.estado = 'FINALIZADO'
""")
    Float getUsoTotalPorUsuariosEnPeriodo(Set<Long> idsUsuarios, LocalDate fechaInicio, LocalDate fechaFin);
}
