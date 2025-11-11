package tpe.microservicios.reporte_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "tpe.microservicios.reporte_service.clients")
public class ReporteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReporteServiceApplication.class, args);
	}

}
