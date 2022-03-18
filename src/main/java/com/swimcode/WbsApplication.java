package com.swimcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WbsApplication.class, args);
    }
}
