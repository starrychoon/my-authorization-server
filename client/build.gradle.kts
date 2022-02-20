plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    // apple silicon issue https://github.com/netty/netty/issues/11020
    implementation(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64")

    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:bootstrap:5.1.1")
    implementation("org.webjars:jquery:3.6.0")
}
