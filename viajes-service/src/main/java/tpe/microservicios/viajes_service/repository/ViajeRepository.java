package tpe.microservicios.viajes_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tpe.microservicios.viajes_service.domains.Viaje;

public interface ViajeRepository extends JpaRepository<Viaje, Long> {
}
