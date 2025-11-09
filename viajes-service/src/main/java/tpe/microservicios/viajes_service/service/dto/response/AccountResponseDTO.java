package tpe.microservicios.viajes_service.service.dto.response;

import tpe.microservicios.viajes_service.utils.AccountType;
import java.util.List;

public record AccountResponseDTO(
        Long idAccount,
        AccountType type,
        float saldo,
        List<Long> idUsers,
        boolean cuentaAnulada
) {
}
