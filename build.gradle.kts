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
	toolVersion = "0.8.8"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
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
		property ("sonar.login", "614e9074d9260a962597cc89a8b8a2e5aedd49dc")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation ("org.junit.jupiter:junit-jupiter:5.9.3")
	testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:5.9.3")
	implementation ("org.jacoco:org.jacoco.core:0.8.8")
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
