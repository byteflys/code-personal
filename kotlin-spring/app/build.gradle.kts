plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    api("io.github.hellogoogle2000:kotlin-commons:1.0.19")
    api("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    api("org.springframework.boot:spring-boot-starter:3.3.5")
    api("org.springframework.boot:spring-boot-starter-web:3.3.5")
    api("org.springframework.boot:spring-boot-starter-data-redis:3.3.5")
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
    api("org.springframework.boot:spring-boot-starter-data-jdbc:3.3.5")
    api("com.mysql:mysql-connector-j:9.1.0")
}