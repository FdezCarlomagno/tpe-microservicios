package tpe.microservicios.accounts_service.service.dto.response;

public record EstadoCuentaDTO(
        Long idUserAccount,
        boolean cuentaAnulada
) {
}
