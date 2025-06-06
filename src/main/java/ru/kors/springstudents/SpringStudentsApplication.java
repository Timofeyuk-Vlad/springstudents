package ru.kors.springstudents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableAsync
public class SpringStudentsApplication {
    @Value("${spring.datasource.password}")
    private String dbPassword;

    public static void main(String[] args) {
        SpringApplication.run(SpringStudentsApplication.class, args);
    }

     @Bean
         // Раскомментируй для проверки
     CommandLineRunner runner() {
         return args -> {
             System.out.println("!!!!!!!!!!!! DB PASSWORD: " + dbPassword);
         };
     }
}