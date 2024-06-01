import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import java.util.*

plugins {
    id("java")

    //id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.intellij.platform") version "2.0.0-beta3"
    //id("org.jetbrains.intellij.platform.migration") version "2.0.0-beta4"

    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    //kotlin("jvm") version "2.0.0"
}

group = "dev.eggnstone.plugins.jetbrains"
version = "2.0.11"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.1.2")

        bundledPlugin("com.intellij.java")

        pluginVerifier()
        zipSigner()
        instrumentationTools()

        testFramework(TestFrameworkType.Platform.JUnit4)
    }

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
/*
intellij {
    version.set("2022.1.4")

    //version.set("2023.3.3")
    // Module was compiled with an incompatible version of Kotlin.
    // The binary version of its metadata is 1.9.0, expected version is 1.7.1.

    type.set("IC") // Target IDE Platform

    plugins.set(listOf())
}
*/

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        //kotlinOptions.jvmTarget = "11"
        compilerOptions {
            //jvmTarget = "11"
        }
    }

    patchPluginXml {
        sinceBuild.set("230")
        untilBuild.set("242.*")
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

tasks.withType<ProcessResources>() {
    doLast {
        //val propertiesFile = file("$buildDir/resources/main/version.properties")
        val output: Provider<RegularFile> = layout.buildDirectory.file("resources/main/version.properties")
        output.get().asFile
        val fileName = output.map { it.asFile.path }
        val propertiesFile = file(fileName)

        propertiesFile.parentFile.mkdirs()
        val properties = Properties()
        properties.setProperty("version", rootProject.version.toString())
        propertiesFile.writer().use { properties.store(it, null) }
    }
}
