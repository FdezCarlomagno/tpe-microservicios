package tpe.microservicios.viajes_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tpe.microservicios.viajes_service.service.dto.response.AccountResponseDTO;
import tpe.microservicios.viajes_service.service.dto.response.EstadoCuentaDTO;
import tpe.microservicios.viajes_service.service.dto.response.UserAccountResponseDTO;

import java.util.List;

@FeignClient(name = "accounts-service", url = "http://localhost:8082/api/accounts")
public interface AccountClient {


    @GetMapping("/{id}")
    AccountResponseDTO getAccountById(@PathVariable("id") long id);

    @PutMapping("/saldo/{id}")
    void updateSaldo(@PathVariable("id") long id, float saldo);

    @GetMapping("/{id}/users")
    List<UserAccountResponseDTO> getUsersFromAccount(@PathVariable("id") long id);

    @GetMapping("/{id}/estado")
    EstadoCuentaDTO isCuentaAnulada(@PathVariable("id") Long id);
}
