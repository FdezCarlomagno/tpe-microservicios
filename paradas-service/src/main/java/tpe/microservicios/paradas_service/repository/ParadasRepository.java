package tpe.microservicios.paradas_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tpe.microservicios.paradas_service.domain.Parada;
public interface ParadasRepository extends JpaRepository<Parada, Long> {

    @Query("SELECT p.id FROM Parada p WHERE :idMonopatin MEMBER OF p.idMonopatines")
    Long findMonopatinEnParada(@Param("idMonopatin") Long idMonopatin);
}
