package ru.kors.springstudents.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${springdoc.server.url:http://localhost:8080}")
  private String serverUrl;

  @Value("${springdoc.server.description:Локальный сервер для разработки}")
  private String serverDescription;

  @Bean
  public OpenAPI customOpenApi() {
    String appTitle = "Spring Students API";
    String appVersion = "v1.0.0";
    String appDescription = "API для управления студентами и связанными с ними сущностями (события, обмены и т.д.).";

    return new OpenAPI()
        .info(new Info()
            .title(appTitle)
            .version(appVersion)
            .description(appDescription)
            .termsOfService("http://example.com/terms/")
            .contact(new Contact()
                .name("Vlad Timofeyuk")
                .url("https://github.com/Timofeyuk-Vlad")
                .email("vlad.timofeyuk@example.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
        .servers(List.of(
            new Server().url(serverUrl).description(serverDescription)
        ));
  }
}