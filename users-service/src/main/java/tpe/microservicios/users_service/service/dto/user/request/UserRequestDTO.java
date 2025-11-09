package tpe.microservicios.users_service.service.dto.user.request;

import java.util.List;

public record UserRequestDTO(
     String nombre,
     String apellido,
     String telefono,
     String email
) {
}
