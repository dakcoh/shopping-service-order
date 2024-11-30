import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("java-library") // Spring Boot 플러그인이 아닌 java-library 플러그인 사용
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral() // 서브프로젝트에서도 저장소 정의
}
tasks {
    // bootJar 태스크 비활성화
    named<BootJar>("bootJar") {
        enabled = false // bootJar 태스크 비활성화
    }
}