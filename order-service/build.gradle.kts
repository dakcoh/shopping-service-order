plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

repositories {
    mavenCentral() // 서브프로젝트에서도 저장소 정의
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // 추가적으로 필요한 의존성 정의
    runtimeOnly("com.h2database:h2") // Order 서비스에서 H2 DB를 사용
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}