/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    application
    id("common.kotlin-library-conventions")
    kotlin("plugin.serialization") version "1.8.0"
}

dependencies {
    api(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation ("com.charleskorn.kaml:kaml:0.51.0")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")
}

application {
    mainClass.set("ServerKt")
}