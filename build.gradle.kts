plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "com.aznos"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

application {
    mainClass.set("com.aznos.MainKt")
}