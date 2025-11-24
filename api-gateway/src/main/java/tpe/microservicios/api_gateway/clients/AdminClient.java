package tpe.microservicios.api_gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tpe.microservicios.api_gateway.service.dto.login.LoginDTO;

@FeignClient(name = "admin-service", url = "http://localhost:8080/api/admin")
public interface AdminClient {
    @PostMapping("/login")
    boolean login(@RequestBody LoginDTO loginDTO);
}
