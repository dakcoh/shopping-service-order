import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("io.spring.dependency-management") version "1.1.6"
	id("org.springframework.boot") version "3.3.2"
	kotlin("jvm") version "1.9.0" apply false
	kotlin("plugin.spring") version "1.9.0"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

group = "com.order"
version = "0.0.1"

repositories {
	mavenCentral()
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("com.h2database:h2:2.3.232") // ver.2024-08-11

		// 모든 서비스에서 공통으로 사용하는 의존성
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")

		// Lombok 설정
		compileOnly("org.projectlombok:lombok")
		implementation("org.projectlombok:lombok:1.18.36") // ver.2024.11-15
		annotationProcessor("org.projectlombok:lombok:1.18.36") // ver.2024.11-15
		testImplementation("org.projectlombok:lombok:1.18.36") // ver.2024.11-15
		testAnnotationProcessor("org.projectlombok:lombok:1.18.36") // ver.2024.11-15

		// 공통 테스트 라이브러리
		implementation("org.jetbrains:annotations:26.0.1") // ver.2024-10-14
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")

		// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-loadbalancer
		implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer:4.1.4")

	}
}

tasks {
	// bootJar 태스크 비활성화
	named<BootJar>("bootJar") {
		enabled = false // bootJar 태스크 비활성화
	}

	named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
		mainClass.set("order.OrderApplication")
		mainClass.set("payment.PaymentApplication")
		mainClass.set("api-gateway.ApiGatewayApplication")
	}
	named<Jar>("jar") {
		enabled = true // jar 활성화
	}
}