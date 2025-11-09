package tpe.microservicios.accounts_service.service.dto.response;

import tpe.microservicios.accounts_service.utils.AccountType;

public record UserAccountResponseDTO(
      String username,
      String user_email,
      AccountType type,
      float saldo
) {
}
