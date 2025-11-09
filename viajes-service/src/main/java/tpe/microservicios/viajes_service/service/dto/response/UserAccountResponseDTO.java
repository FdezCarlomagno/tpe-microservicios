package tpe.microservicios.viajes_service.service.dto.response;


import tpe.microservicios.viajes_service.utils.AccountType;

public record UserAccountResponseDTO(
        String username,
        String user_email,
        AccountType type,
        float saldo
) {
}
