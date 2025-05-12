plugins {
    id("dev.isxander.modstitch.base") version "0.5.12"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var loader: String = name.split("-")[1]
stonecutter {
    consts(
        "fabric" to (loader == "fabric"),
        "neoforge" to (loader == "neoforge"),
        "forge" to (loader == "forge")
    )
}

modstitch {
    minecraftVersion = minecraft

    // Alternatively use stonecutter.eval if you have a lot of versions to target.
    // https://stonecutter.kikugie.dev/stonecutter/guide/setup#checking-versions
    javaTarget = when (minecraft) {
        "1.20.1" -> 17
        "1.21.1" -> 21
        "1.21.3" -> 21
        else -> throw IllegalArgumentException("Please store the java version for ${property("deps.minecraft")} in build.gradle.kts!")
    }

    // If parchment doesn't exist for a version yet, you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        prop("mod.id") { modId = it }
        prop("mod.name") { modName = it }
        prop("mod.version") { modVersion = it }
        prop("mod.group") { modGroup = it }
        prop("mod.authors") {
            modAuthor = if (loader == "fabric") it.split(", ").joinToString("\",\"") else it
        }
        prop("mod.license") { modLicense = it }
        prop("mod.description") { modDescription = it }

        fun <K, V> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            prop("mod.homepage") { put("mod_homepage", it) }
            prop("mod.sources") { put("mod_sources", it) }
            prop("mod.issues") { put("mod_issues", it) }
            prop("deps.minecraft_range") { put("minecraft_range", it) }
        }
    }

    // Fabric Loom (Fabric)
    loom {
        prop("deps.fabric_loader") { fabricLoaderVersion = it }

        // Configure loom like normal in this block.
        configureLoom {
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
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        enable {
            prop("deps.forge") { forgeVersion = it }
            prop("deps.neoforge") { neoForgeVersion = it }
        }

        configureNeoforge {
            runs {
                register("neoforgeClient") {
                    client()
                    gameDirectory = layout.projectDirectory.dir("../../run")
                }
            }
        }
    }

    mixin {
        if (loader != "fabric") {
            addMixinsToModManifest = true
            prop("mod.id") { configs.register(it) }
        } else {
            addMixinsToModManifest = false
        }
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    fun Dependency?.jij() = this?.also(::modstitchJiJ)

    if (modstitch.isLoom) {
        prop("deps.fapi") { modstitchModApi("net.fabricmc.fabric-api:fabric-api:${it}") }
        prop("deps.modmenu") { modstitchModApi("com.terraformersmc:modmenu:${it}") }
    }

    if (modstitch.isModDevGradleLegacy) {
        compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)
        implementation("io.github.llamalad7:mixinextras-forge:0.4.1").jij()
        prop("deps.yacl") { compileOnly("dev.isxander:yet-another-config-lib:${it}-${loader}") }
    } else {
        prop("deps.yacl") { modstitchModApi("dev.isxander:yet-another-config-lib:${it}-${loader}") }
    }

    // Anything else in the dependencies block will be used for all platforms.
}