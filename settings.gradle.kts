pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        // Modstitch
        maven("https://maven.isxander.dev/releases/")

        // Loom platform
        maven("https://maven.fabricmc.net/")

        // MDG platform
        maven("https://maven.neoforged.net/releases/")

        // Stonecutter
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    kotlin("jvm") version "2.4.0" apply false
    id("com.google.devtools.ksp") version "2.3.9" apply false
    id("dev.kikugie.stonecutter") version "0.9.6"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        /**
         * @param mcVersion The base minecraft version.
         * @param loaders A list of loaders to target, supports "fabric" (1.14+), "neoforge"(1.20.6+), "vanilla"(any) or "forge"(<=1.20.1)
         */
        fun mc(mcVersion: String, loaders: Iterable<String>) =
            loaders.forEach { version("$mcVersion-$it", mcVersion) }

        fun ml(mcVersions: Iterable<String>) =
            mcVersions.forEach { version(it, it)
                .buildscript("build-multiloader.gradle.kts") }

        // Configure your targets here!
        ml(listOf(
            "26.2",
            "26.1"
        ))
        mc("1.21.11", loaders = listOf("fabric", "neoforge"))
        mc("1.21.10", loaders = listOf("fabric", "neoforge"))
        mc("1.21.6", loaders = listOf("fabric", "neoforge"))
        mc("1.21.4", loaders = listOf("fabric", "neoforge"))
        mc("1.21.3", loaders = listOf("fabric", "neoforge"))
        mc("1.21.1", loaders = listOf("fabric", "neoforge"))

        // This is the default target.
        // https://stonecutter.kikugie.dev/stonecutter/guide/setup#settings-settings-gradle-kts
        vcsVersion = "26.2"
    }
}

rootProject.name = "BigSignWriter"

