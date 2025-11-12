// src/main/java/tpe/microservicios/admin_service/clients/CuentaClient.java
package tpe.microservicios.admin_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-client", url = "http://localhost:8082/api/cuentas")
public interface UserClient {

    @DeleteMapping("/{id}")
    void anular(@PathVariable("id") Long id);
}