package com.example.main;

import javax.persistence.Entity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@EntityScan(basePackages = {"com.example.model"})
public class AshionShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(AshionShopApplication.class, args);
	}

}
