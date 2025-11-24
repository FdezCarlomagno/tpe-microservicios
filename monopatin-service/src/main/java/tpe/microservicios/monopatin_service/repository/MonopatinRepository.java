package tpe.microservicios.monopatin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.monopatin_service.domain.Monopatin;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;
import tpe.microservicios.monopatin_service.clients.ParadaClient;

import java.util.List;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    @Query("SELECT m FROM Monopatin m WHERE m.estado=ACTIVADO")
    List<Monopatin> findAllDisponibles();

    @Query("SELECT DISTINCT m FROM Monopatin m WHERE m.idParada = :idParada")
    List<Monopatin> findMonopatinesByParada(Long idParada);
}
