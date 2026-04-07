plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    dependencies {
        // Ktor Server
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.netty)
        implementation(libs.ktor.server.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        // Database
        implementation(libs.exposed.core)
        implementation(libs.exposed.jdbc)
        implementation(libs.h2)

        // Logging
        implementation(libs.logback.classic)

        // Testing
        testImplementation(libs.ktor.server.test.host) // This replaces ktor-server-tests
        testImplementation(libs.kotlin.test.junit)
    }
}
