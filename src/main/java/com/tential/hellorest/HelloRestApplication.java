package com.tential.hellorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HelloRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloRestApplication.class, args);
    }
}
