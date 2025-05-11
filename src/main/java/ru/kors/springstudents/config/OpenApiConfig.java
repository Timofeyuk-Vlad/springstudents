package ru.kors.springstudents.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

  // Можно вынести в application.yaml, если нужно менять для разных окружений
  @Value("${springdoc.server.url:http://localhost:8080}") // Значение по умолчанию
  private String serverUrl;

  @Value("${springdoc.server.description:Локальный сервер для разработки}") // Значение по умолчанию
  private String serverDescription;

  @Bean
  public OpenAPI customOpenAPI(
      // Можно внедрить значения из application.yaml через @Value, если они там определены
      // @Value("${app.version:1.0.0}") String appVersion,
      // @Value("${app.title:Мой API Студентов}") String appTitle,
      // @Value("${app.description:API для управления студентами и их активностями}") String appDescription
  ) {
    // Если используешь @Value для title, version, description, замени строки ниже
    String appTitle = "Spring Students API";
    String appVersion = "v1.0.0";
    String appDescription = "API для управления студентами и связанными с ними сущностями (события, обмены и т.д.). Проект для лабораторных работ.";

    return new OpenAPI()
        .info(new Info()
            .title(appTitle)
            .version(appVersion)
            .description(appDescription)
            .termsOfService("http://example.com/terms/") // Замени на реальную ссылку, если есть
            .contact(new Contact()
                .name("Vlad Timofeyuk") // Твое имя
                .url("https://github.com/Timofeyuk-Vlad") // Ссылка на твой GitHub или другой ресурс
                .email("vlad.timofeyuk@example.com")) // Твой email
            .license(new License()
                .name("Apache 2.0") // Или другая лицензия
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
        .servers(List.of(
            new Server().url(serverUrl).description(serverDescription)
            // Можно добавить другие серверы, например, для тестового или продакшн окружения
            // new Server().url("https://api.prod.example.com").description("Production server")
        ));
  }
}