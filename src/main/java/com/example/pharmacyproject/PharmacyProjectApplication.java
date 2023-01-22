package com.example.pharmacyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PharmacyProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyProjectApplication.class, args);
    }

}
