import org.jetbrains.kotlin.gradle.tasks.*
import org.jlleitschuh.gradle.ktlint.*
import org.jlleitschuh.gradle.ktlint.reporter.*

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")

    id("io.spring.dependency-management")
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    repositories {
        mavenCentral()
    }

    configure<KtlintExtension> {
        debug.set(true)
        verbose.set(true)
        reporters {
            reporter(ReporterType.JSON)
            reporter(ReporterType.HTML)
        }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    group = "io.starrychoon"
    version = "1.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencyManagement {
        val coroutines_version: String by project
        val kotlin_logging_version: String by project
        val spring_authorization_server_version: String by project
        val bouncy_castle_version: String by project

        dependencies {
            dependencySet("org.jetbrains.kotlinx:$coroutines_version") {
                entry("kotlinx-coroutines-core-jvm")
                entry("kotlinx-coroutines-reactive")
                entry("kotlinx-coroutines-reactor")
            }
            dependency("io.github.microutils:kotlin-logging-jvm:$kotlin_logging_version")

            dependency(
                mapOf(
                    "group" to "org.springframework.security",
                    "name" to "spring-security-oauth2-authorization-server",
                    "version" to spring_authorization_server_version,
                ),
            )

            dependency("org.bouncycastle:bcpkix-jdk15on:$bouncy_castle_version")
        }
    }
}

tasks.named("ktlintApplyToIdeaGlobally") {
    enabled = false
    println("Do not override the global style file.")
}
