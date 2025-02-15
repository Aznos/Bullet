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
    testImplementation(kotlin("test"))

    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("io.ktor:ktor-network:2.3.7")
}

application {
    mainClass = "com.aznos.BulletKt"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}