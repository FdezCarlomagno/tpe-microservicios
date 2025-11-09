package tpe.microservicios.accounts_service.service.dto.request;

import tpe.microservicios.accounts_service.utils.AccountType;

import java.time.LocalDate;
import java.util.List;

public record UserAccountRequestDTO(
     AccountType type,
     List<Long> idUsers
) {
}
