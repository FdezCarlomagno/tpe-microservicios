package tpe.microservicios.admin_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tpe.microservicios.admin_service.domain.Administrador;

public interface AdminRepository extends JpaRepository<Administrador, Long > {
    boolean existsByEmailAndPassword(String email, String password);
}
