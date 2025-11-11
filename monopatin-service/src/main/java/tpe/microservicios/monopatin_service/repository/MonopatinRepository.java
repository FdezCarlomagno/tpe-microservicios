package tpe.microservicios.monopatin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.monopatin_service.domain.Monopatin;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.monopatin_service.clients.ParadaClient;

import java.util.List;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    @Query("SELECT m.id, m.idParada, m.disponible FROM Monopatin m WHERE m.estado=ACTIVADO")
    List<Monopatin> findAllDisponibles();
}
