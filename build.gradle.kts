plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.2"
    //id("org.jetbrains.intellij") version "1.17.0" => Unsupported class file major version 63
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
}

group = "dev.eggnstone.plugins.jetbrains"
version = "2.0.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.4")

    //version.set("2023.3.3")
    // Module was compiled with an incompatible version of Kotlin.
    // The binary version of its metadata is 1.9.0, expected version is 1.7.1.

    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
        sinceBuild.set("221")
        untilBuild.set("233.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

tasks.withType<Test> {
    exclude("**/*jetbrainsplugins*")
}
