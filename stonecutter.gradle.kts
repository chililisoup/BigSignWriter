plugins {
    id("dev.kikugie.stonecutter")

    val modstitchVersion = "0.7.1-unstable"
    id("dev.isxander.modstitch.base") version modstitchVersion apply false
    id("dev.isxander.modstitch.shadow") version modstitchVersion apply false
}
stonecutter active "1.21.9-fabric"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven {
            name = "Xander Maven"
            url = uri("https://maven.isxander.dev/releases")
        }
        maven {
            name = "Terraformers"
            url = uri("https://maven.terraformersmc.com/")
        }
        maven {
            name = "Kotlin for Forge"
            url = uri("https://thedarkcolour.github.io/KotlinForForge/")
            content {
                includeGroup("thedarkcolour")
            }
        }
    }
}