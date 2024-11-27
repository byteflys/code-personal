plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
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
    // commons
    api("io.github.hellogoogle2000:kotlin-commons:1.0.19")
    // kotlin
    api("org.jetbrains.kotlin:kotlin-reflect:2.0.21")
    // spring
    api("org.springframework.boot:spring-boot-starter:3.3.5")
    api("org.springframework.boot:spring-boot-starter-web:3.3.5")
    // database
    api("org.springframework.boot:spring-boot-starter-data-jdbc:3.3.5")
    api("com.mysql:mysql-connector-j:9.1.0")
    // jpa
    api("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
    // query dsl
    api("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    api("com.querydsl:querydsl-kotlin:5.1.0")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    // mongodb
    api("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.5")
    // redis
    api("org.springframework.boot:spring-boot-starter-data-redis:3.3.5")
}