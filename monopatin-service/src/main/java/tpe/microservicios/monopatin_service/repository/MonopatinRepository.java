package tpe.microservicios.monopatin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.monopatin_service.domain.Monopatin;
import tpe.microservicios.monopatin_service.service.dto.response.MonopatinResponseDTO;

import java.util.List;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    @Query("SELECT new tpe.microservicios.monopatin_service.service.dto.MonopatinResponseDTO(" +
            "m.id, m.parada, m.disponible) FROM Monopatin m")
    List<MonopatinResponseDTO> findAllDisponibles();
}
