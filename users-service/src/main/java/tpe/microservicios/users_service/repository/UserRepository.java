package tpe.microservicios.users_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tpe.microservicios.users_service.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {  // Cambio: JpaRepository → MongoRepository, Long → String

    boolean existsByEmail(String email);

    // Métodos adicionales útiles
    Optional<User> findByEmail(String email);

    // Consulta personalizada si necesitas
    @Query("{ 'email': ?0 }")
    List<User> findUsersByEmail(String email);

    // Para verificar email excluyendo un usuario específico (útil en updates)
    boolean existsByEmailAndIdNot(String email, String id);
}