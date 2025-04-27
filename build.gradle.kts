import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.Test // Импорт для Test
import org.gradle.testing.jacoco.tasks.JacocoReport // Импорт для JacocoReport

plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"

	checkstyle
	id("org.sonarqube") version "6.0.1.5171"
	id("jacoco")
}

group = "ru.kors"
version = "0.0.1-SNAPSHOT"

jacoco {
	toolVersion = "0.8.11"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

checkstyle {
	toolVersion = "10.21.2"
}

sonarqube {
	properties {
		property("sonar.projectKey", "Timofeyuk-Vlad_springstudents")
		property("sonar.organization", "timofeyuk-vlad")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.mapstruct:mapstruct:1.5.5.Final")

	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	runtimeOnly("org.postgresql:postgresql")

	testRuntimeOnly("com.h2database:h2") // Оставляем H2 для тестов

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


