package com.famicare.FamicareBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FamicareBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamicareBackendApplication.class, args);
    }

}
