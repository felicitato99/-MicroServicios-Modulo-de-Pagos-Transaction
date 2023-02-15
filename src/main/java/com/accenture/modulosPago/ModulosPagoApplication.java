package com.accenture.modulosPago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class ModulosPagoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModulosPagoApplication.class, args);
	}

}

