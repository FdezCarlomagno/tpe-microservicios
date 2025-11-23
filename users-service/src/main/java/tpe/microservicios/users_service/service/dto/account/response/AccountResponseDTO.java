package tpe.microservicios.users_service.service.dto.account.response;

import tpe.microservicios.users_service.utils.AccountType;

import java.util.List;

public record AccountResponseDTO(
        Long idAccount,
        AccountType type,
        float saldo,
        List<Long> idUsers,
        boolean anulada
) {
}