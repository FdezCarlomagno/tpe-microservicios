package tpe.microservicios.paradas_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "tpe.microservicios.paradas_service.clients")
public class ParadasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParadasServiceApplication.class, args);
	}

}
