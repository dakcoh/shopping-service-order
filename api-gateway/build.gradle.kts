plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

repositories {
    mavenCentral() // 서브프로젝트에서도 저장소 정의
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis-reactive
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.4.1")

    // https://mvnrepository.com/artifact/io.lettuce/lettuce-core
    implementation("io.lettuce:lettuce-core:6.5.1.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-security")

    // 추가적으로 필요한 의존성 정의
    runtimeOnly("com.h2database:h2") // Order 서비스에서 H2 DB를 사용
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}