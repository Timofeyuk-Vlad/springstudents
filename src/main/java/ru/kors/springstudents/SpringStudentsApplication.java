package ru.kors.springstudents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringStudentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringStudentsApplication.class, args);
    }
}