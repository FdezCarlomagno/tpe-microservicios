package tpe.microservicios.admin_service.service.dto.response;

import tpe.microservicios.admin_service.utils.AccountType;

import java.util.List;

public record AccountResponseDTO(
        Long idAccount,
        AccountType type,
        float saldo,
        List<Long> idUsers,
        boolean anulada
) {
}