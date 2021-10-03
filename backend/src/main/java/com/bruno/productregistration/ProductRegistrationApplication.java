package com.bruno.productregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProductRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductRegistrationApplication.class, args);
	}

}
