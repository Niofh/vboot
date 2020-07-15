package com.example.redisdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RedisdemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(RedisdemoApplication.class, args);
    }

}


