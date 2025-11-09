package tpe.microservicios.accounts_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tpe.microservicios.accounts_service.service.dto.response.UserResponseDTO;

@FeignClient(name = "users-service", url = "http://localhost:8081/api/users")
public interface UserClient {

    @GetMapping("/{id}")
    UserResponseDTO getUserById(@PathVariable Long id);
}
