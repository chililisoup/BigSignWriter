plugins {
    `maven-publish`
    id("fabric-loom")
}

class ModData {
    val id = property("mod.id").toString()
    val name = property("mod.name").toString()
    val archiveName = property("mod.archive_name").toString()
    val version = property("mod.version").toString()
    val group = property("mod.group").toString()
}

class ModDependencies {
    operator fun get(name: String) = property("deps.$name").toString()
}

val mod = ModData()
val deps = ModDependencies()
val mcVersion = stonecutter.current.version
val mcDep = property("mod.mc_dep").toString()

version = "${mod.version}+$mcVersion"
group = mod.group
base { archivesName.set(mod.archiveName) }

repositories {
    maven {
        name = "Xander Maven"
        url = uri("https://maven.isxander.dev/releases")
    }
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com/")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings("net.fabricmc:yarn:$mcVersion+build.${deps["yarn_build"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${deps["fabric_loader"]}")

    modCompileOnlyApi("com.terraformersmc:modmenu:${deps["modmenu"]}")
    modCompileOnly("dev.isxander:yet-another-config-lib:${deps["yacl"]}")
}

loom {
    decompilers {
        get("vineflower").apply { // Adds names to lambdas - useful for mixins
            options.put("mark-corresponding-synthetics", "1")
        }
    }

    runConfigs.all {
        ideConfigGenerated(false)
    }

    runs {
        register("fabricClient") {
            client()
            name = "Fabric Client"
            vmArgs("-Dmixin.debug.export=true")
            runDir = "../../run"
            ideConfigGenerated(true)
        }
    }
}

java {
    val java = when {
        stonecutter.eval(stonecutter.current.version, ">=1.20.5") -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_17
    }

    targetCompatibility = java
    sourceCompatibility = java
}

tasks.processResources {
    val javaCompat = when {
        stonecutter.eval(stonecutter.current.version, ">=1.20.5") -> "JAVA_21"
        else -> "JAVA_17"
    }

    inputs.property("id", mod.id)
    inputs.property("name", mod.name)
    inputs.property("version", mod.version)
    inputs.property("mcDep", mcDep)
    inputs.property("javaCompat", javaCompat)

    val map = mapOf(
        "id" to mod.id,
        "name" to mod.name,
        "version" to mod.version,
        "mcDep" to mcDep,
        "javaCompat" to javaCompat
    )

    filesMatching("fabric.mod.json") { expand(map) }
    filesMatching("*.mixins.json") { expand(map) }
}

tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.get().archiveFile)
    into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
    dependsOn("build")
}
