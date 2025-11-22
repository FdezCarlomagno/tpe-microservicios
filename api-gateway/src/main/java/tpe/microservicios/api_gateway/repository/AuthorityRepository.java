package tpe.microservicios.api_gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpe.microservicios.api_gateway.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
