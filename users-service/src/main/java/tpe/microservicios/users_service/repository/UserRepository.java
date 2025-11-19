package tpe.microservicios.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tpe.microservicios.users_service.domain.User;
import tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    //metodos comunes
    /*@Query("SELECT new tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO(" +
            "u.id, u.nombre, u.apellido, u.telefono, u.email) FROM User u")
    List<UserResponseDTO> findUsers();

    @Query("SELECT new tpe.microservicios.users_service.service.dto.user.response.UserResponseDTO(" +
            "u.id, u.nombre, u.apellido, u.telefono, u.email) FROM User u WHERE u.id = :user_id")
    UserResponseDTO findById(@Param("user_id") long user_id);*/

}
