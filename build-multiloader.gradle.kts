plugins {
    id("idea")
    id("dev.kikugie.stonecutter")
    id("dev.isxander.modstitch.base")
    id("mod-build-common")
}

val mod = `mod-common`.mod.get()
val deps = mod.deps

stonecutter {
    constants["fabric"] = true
    constants["neoforge"] = true
}

modstitch {
    minecraftVersion = deps.minecraft
    javaVersion = deps.javaVersion

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

    compileOnly("net.neoforged.fancymodloader:loader:${deps.neoForgeLoader}")
    deps.neoForge { compileOnly("net.neoforged:neoforge:$it") }
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
        dependsOn("convertClassTweaker")
    }

    register<Copy>("convertClassTweaker") {
        description = "Copies the class tweaker and converts it to an access transformer"
        group = "build"

        from(layout.buildDirectory.file("resources/main/${mod.id}.ct"))
        into(layout.buildDirectory.dir("resources/main/META-INF"))
        rename("${mod.id}\\.ct", "accesstransformer.cfg")
        filter { line -> line
            .replace("classTweaker v1 official", "")
            .replace(Regex("accessible \\w+"), "public")
            .replace(Regex("(?<=public [\\w./]+)\\/"), ".")
            .replace(" (", "(")
        }

        dependsOn("processResources")
    }
}
