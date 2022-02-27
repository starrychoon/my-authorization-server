import org.jetbrains.kotlin.gradle.tasks.*
import org.jlleitschuh.gradle.ktlint.*
import org.jlleitschuh.gradle.ktlint.reporter.*

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
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

    group = "io.starrychoon"
    version = "1.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.named("ktlintApplyToIdeaGlobally") {
    enabled = false
    println("Do not override the global style file.")
}
