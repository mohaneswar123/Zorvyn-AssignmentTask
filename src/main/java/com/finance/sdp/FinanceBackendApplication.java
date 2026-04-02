package com.finance.sdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinanceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceBackendApplication.class, args);
		System.out.println("Finance Backend Application started successfully!");
	}

}
