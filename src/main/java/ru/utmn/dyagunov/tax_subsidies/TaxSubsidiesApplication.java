package ru.utmn.dyagunov.tax_subsidies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.lang.System;

@SpringBootApplication
public class TaxSubsidiesApplication {

	public static void main(String[] args) {

        SpringApplication.run(TaxSubsidiesApplication.class, args);
        System.out.println("Ок");
	}
}
