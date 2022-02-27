rootProject.name = "my-authorization-server"

include("authorization", "client", "resource")

pluginManagement {
    val kotlin_version: String by settings
    val ktlint_version: String by settings
    val spring_boot_version: String by settings
    val spring_dependency_management_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.spring") version kotlin_version apply false
        kotlin("plugin.jpa") version kotlin_version apply false

        id("org.springframework.boot") version spring_boot_version apply false
        id("io.spring.dependency-management") version spring_dependency_management_version apply false

        id("org.jlleitschuh.gradle.ktlint") version ktlint_version
        id("org.jlleitschuh.gradle.ktlint-idea") version ktlint_version
    }
}
