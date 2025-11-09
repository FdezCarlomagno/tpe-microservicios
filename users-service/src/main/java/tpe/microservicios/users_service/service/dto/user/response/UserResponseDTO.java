package tpe.microservicios.users_service.service.dto.user.response;

public record UserResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String telefono,
        String email
) {
}
