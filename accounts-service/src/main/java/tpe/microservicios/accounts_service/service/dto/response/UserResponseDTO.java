package tpe.microservicios.accounts_service.service.dto.response;

public record UserResponseDTO(
        String nombre,
        String apellido,
        String telefono,
        String email
) {
}
