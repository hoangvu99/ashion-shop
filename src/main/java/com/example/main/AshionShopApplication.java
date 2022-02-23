package com.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class AshionShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AshionShopApplication.class, args);
	}

}
