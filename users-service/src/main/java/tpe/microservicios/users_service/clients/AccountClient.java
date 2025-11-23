package tpe.microservicios.users_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tpe.microservicios.users_service.service.dto.account.response.AccountResponseDTO;

@FeignClient(name = "accounts-service", url = "http://localhost:8082/api/accounts")
public interface AccountClient {

    @PutMapping("/{id}/unlink-user")
    void unlinkUser(
        @PathVariable("id") long idAccount,
        @RequestBody String idUser
    );

    @GetMapping("/users/{id}/account")
    AccountResponseDTO getAccountByUserID(
            @PathVariable("id") String id
    );
}
