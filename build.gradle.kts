import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.Test // Импорт для Test
import org.gradle.testing.jacoco.tasks.JacocoReport // Импорт для JacocoReport

val springBootVersion = "3.4.3"
val springDependencyManagementVersion = "1.1.7"
val sonarQubePluginVersion = "6.0.1.5171"
val jacocoToolVersion = "0.8.11"
val checkstyleToolVersion = "10.21.2"
val mapstructVersion = "1.5.5.Final"
val springdocOpenapiVersion = "2.8.8" // Обновил на актуальную, если нужно другую - поменяй
val lombokVersion = "1.18.36"
val lombokMapstructBindingVersion = "0.2.0"

plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	//kotlin("jvm") version "1.9.23"

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

 //Конфигурация для ВСЕХ конфигураций зависимостей
//configurations.all {
//	resolutionStrategy {
//		// Отключаем верификацию полностью
//		disableDependencyVerification()
//	}
//}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	//implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	//implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiVersion")
	//implementation("com.example:adependency:1.0")

	//compileOnly("org.projectlombok:lombok:1.18.36")
	//annotationProcessor("org.projectlombok:lombok:1.18.36")
	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")

	//annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	//annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")

	runtimeOnly("org.postgresql:postgresql")
	//runtimeOnly("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:6.0.1.5171")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")

	testRuntimeOnly("com.h2database:h2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
