import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.Test

val springBootVersion = "3.4.3"
val springDependencyManagementVersion = "1.1.7"
val sonarQubePluginVersion = "6.0.1.5171"
val jacocoToolVersion = "0.8.11"
val checkstyleToolVersion = "10.21.2"
val mapstructVersion = "1.5.5.Final"
val springdocOpenapiVersion = "2.8.8"
val lombokVersion = "1.18.36"
val lombokMapstructBindingVersion = "0.2.0"
val owaspEncoderVersion = "1.2.3"

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
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiVersion")
	implementation("org.owasp.encoder:encoder:$owaspEncoderVersion")

	compileOnly("org.projectlombok:lombok:$lombokVersion")
	annotationProcessor("org.projectlombok:lombok:$lombokVersion")

	annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:$lombokMapstructBindingVersion")
	testCompileOnly ("org.projectlombok:lombok")
	testAnnotationProcessor ("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
		exceptionFormat = TestExceptionFormat.FULL
		showStandardStreams = true
	}
}