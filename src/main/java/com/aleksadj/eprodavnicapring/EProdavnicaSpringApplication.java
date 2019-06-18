package com.aleksadj.eprodavnicapring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("model")
public class EProdavnicaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EProdavnicaSpringApplication.class, args);
    }

}
