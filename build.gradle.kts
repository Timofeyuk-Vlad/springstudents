import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

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

 // Оставляем закомментированным
checkstyle {
    toolVersion = "10.21.2"
}


 // Оставляем закомментированным
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
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	// MapStruct
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	// База данных
	runtimeOnly("org.postgresql:postgresql")

	// DevTools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Тестирование
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


// Оставляем упрощенный блок тестов
tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
		exceptionFormat = TestExceptionFormat.FULL
		showStandardStreams = true
	}
	finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
	dependsOn(tasks.named("test"))
	reports {
		xml.required.set(true) // XML отчет нужен для SonarQube
		html.required.set(true) // HTML для локального просмотра
	}
}


tasks.withType<JavaCompile>().configureEach {
	options.compilerArgs.add("-Amapstruct.defaultComponentModel=spring")
}