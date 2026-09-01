plugins {
    id("idea")
    id("dev.kikugie.stonecutter")
    id("dev.isxander.modstitch.base")
    id("mod-build-common")
}

val mod = `mod-common`.mod.get()
val deps = mod.deps

stonecutter {
    constants {
        match(name.split("-")[1], "fabric", "neoforge")
    }
}

modstitch {
    minecraftVersion = deps.minecraft
    javaVersion = deps.javaVersion
    parchment { deps.parchment { mappingsVersion = it } }

    metadata {
        modId = mod.id
        modVersion = mod.archiveVersion
    }

    // Fabric Loom (Fabric)
    loom {
        fabricLoaderVersion = deps.fabricLoader

        // Configure loom like normal in this block.
        configureLoom {
            runConfigs.all {
                ideConfigGenerated(false)
            }

            runs {
                register("testClient") {
                    client()
                    name = "Test Client"
                    vmArgs("-Dmixin.debug.export=true")
                    runDir = "../../run"
                    ideConfigGenerated(true)
                }
            }

            mixin.useLegacyMixinAp = false
        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        deps.neoForge { neoForgeVersion = it }

        configureNeoForge {
            runs {
                register("testClient") {
                    client()
                    gameDirectory = layout.projectDirectory.dir("../../run")
                }
            }
        }
    }

    mixin {
        addMixinsToModManifest = true
        configs.register(mod.id)
    }
}

dependencies {
    deps.fabricApi { modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${it}") }
    deps.modMenu { modstitchModImplementation("com.terraformersmc:modmenu:${it}") }
}

java {
    targetCompatibility = deps.java
    sourceCompatibility = deps.java
}

modstitch.onEnable {
    modstitch.moddevgradle {
        tasks.named("createMinecraftArtifacts") {
            dependsOn("stonecutterGenerate")
        }
    }
}

tasks {
    named("jar") {
        dependsOn("filterArtifacts")
    }

    register<Delete>("filterArtifacts") {
        description = "Deletes meta files irrelevant to the target platform"

        if (modstitch.isLoom)
            delete(layout.buildDirectory.file("resources/main/META-INF/neoforge.mods.toml"))
        else
            delete(layout.buildDirectory.file("resources/main/fabric.mod.json"))
    }
}
