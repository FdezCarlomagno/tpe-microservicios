package tpe.microservicios.reporte_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpe.microservicios.reporte_service.entity.Mantenimiento;

@Repository
public interface MantenimientoRespository extends JpaRepository<Mantenimiento, Integer> {
}
