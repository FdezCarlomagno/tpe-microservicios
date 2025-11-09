package tpe.microservicios.paradas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tpe.microservicios.paradas_service.domain.Parada;
public interface ParadasRepository extends JpaRepository<Parada, Long> {

    @Query("SELECT p.id FROM Parada p WHERE :idMonopatin IN elements(p.idMonopatines)")
    // Devuelve la id de la parada donde se encuentra el monopatin
    Long findMonopatinEnParada(Long idMonopatin);
}
