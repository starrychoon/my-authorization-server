import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
}

subprojects {
    apply(plugin = "kotlin")

    group = "io.starrychoon"
    version = "1.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
    }

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
