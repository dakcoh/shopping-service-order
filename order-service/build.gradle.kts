import java.net.Socket

plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

repositories {
    mavenCentral() // 서브프로젝트에서도 저장소 정의
}

dependencies {
    // 공통 DTO 모듈 사용
    implementation(project(":shared-dto"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // 추가적으로 필요한 의존성 정의
    runtimeOnly("com.h2database:h2") // Order 서비스에서 H2 DB를 사용

    // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
    implementation("org.springframework.kafka:spring-kafka:3.3.0")
    // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka-test
    testImplementation("org.springframework.kafka:spring-kafka-test:3.3.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.11.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.test {
    useJUnitPlatform()
}

// 1) Kafka 실행 여부를 확인하는 태스크 정의
tasks.register("checkKafka") {
    doLast {
        try {
            // Kafka의 기본 포트인 9092로 연결 시도
            Socket("localhost", 9092).use {
                project.extensions.extraProperties["isKafkaRunning"] = true
            }
        } catch (e: Exception) {
            // 연결 실패 시 false로 설정
            project.extensions.extraProperties["isKafkaRunning"] = false
        }
    }
}