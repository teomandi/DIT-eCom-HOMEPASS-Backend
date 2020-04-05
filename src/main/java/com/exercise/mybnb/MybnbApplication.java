package com.exercise.mybnb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MybnbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybnbApplication.class, args);
    }

}
