package tpe.microservicios.viajes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "tpe.microservicios.viajes_service.clients")
public class ViajesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViajesServiceApplication.class, args);
	}

}
