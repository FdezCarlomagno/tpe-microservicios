package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tpe.microservicios.admin_service.service.dto.response.AccountResponseDTO;

@FeignClient(name = "accounts-service", url = "http://localhost:8082/api/accounts")
public interface AccountClient {

    @PutMapping("/{id}/anular")
    AccountResponseDTO anularCuenta(@PathVariable("id") Long idAccount);

    @GetMapping("/{id}")
    AccountResponseDTO getAccount(@PathVariable("id") Long idAccount);
}