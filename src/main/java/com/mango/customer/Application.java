package com.mango.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		System.out.println("Hello mango!");
		SpringApplication.run(Application.class, args);
	}
}
