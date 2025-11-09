package tpe.microservicios.monopatin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "tpe.microservicios.monopatin_service.clients")
public class MonopatinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonopatinServiceApplication.class, args);
	}

}
