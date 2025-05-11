package ru.kors.springstudents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.EnableAspectJAutoProxy; // Если нужно

@SpringBootApplication
// @EnableAspectJAutoProxy // Раскомментируй, если аспекты не работают
public class SpringStudentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringStudentsApplication.class, args);
    }

}
