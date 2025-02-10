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
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.14")
}

application {
    mainClass.set("com.aznos.MainKt")
}