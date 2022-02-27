plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    val kotlin_logging_version: String by project
    val spring_authorization_server_version: String by project
    val bouncy_castle_version: String by project

    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(
        group = "org.springframework.security",
        name = "spring-security-oauth2-authorization-server",
        version = spring_authorization_server_version,
    )
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.bouncycastle:bcpkix-jdk15on:$bouncy_castle_version")
}
