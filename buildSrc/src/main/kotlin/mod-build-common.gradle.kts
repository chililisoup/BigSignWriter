@file:Suppress("unused")

plugins {
    idea
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

class ModDeps {
    val fabricLoader = project.property("deps.fabric_loader") as String
    val neoForgeLoader = project.property("deps.neoforge_loader") as String
    val minecraft = project.property("deps.minecraft") as String

    val minecraftRangeNeoForge = project.property("deps.minecraft_range") as String
    val minecraftRangeFabric = this.getFabricMinecraftRange(minecraftRangeNeoForge)

    fun fabricApi(consumer: (prop: String) -> Unit) = prop("deps.fapi", consumer)
    fun neoForge(consumer: (prop: String) -> Unit) = prop("deps.neoforge", consumer)
    fun modMenu(consumer: (prop: String) -> Unit) = prop("deps.modmenu", consumer)
    fun parchment(consumer: (prop: String) -> Unit) = prop("deps.parchment", consumer)

    val java = when {
        minecraft >= "26" -> JavaVersion.VERSION_25
        minecraft >= "1.20.6" -> JavaVersion.VERSION_21
        minecraft >= "1.18" -> JavaVersion.VERSION_17
        minecraft >= "1.17" -> JavaVersion.VERSION_16
        else -> JavaVersion.VERSION_1_8
    }
    val javaVersion = java.ordinal + 1

    private fun getFabricMinecraftRange(minecraftRange: String): String {
        if (minecraft < "26") return minecraftRange
        val splitMinecraftRange = minecraftRange.subSequence(1, minecraftRange.length - 1).split(",")
        return ">=${splitMinecraftRange[0]} <${splitMinecraftRange[1]}"
    }
}

class ModData {
    val version = property("mod.version") as String
    val id = property("mod.id") as String
    val name = property("mod.name") as String
    val authors = property("mod.authors") as String
    val contributors = property("mod.contributors") as String
    val description = property("mod.description") as String
    val homepage = property("mod.homepage") as String
    val sources = property("mod.sources") as String
    val issues = property("mod.issues") as String
    val license = property("mod.license") as String

    val deps = ModDeps()

    val archiveVersion = "${this.version}+${project.name}"

    fun getProps(): Map<String, String> = buildMap {
        put("mod_id", id)
        put("mod_name", name)
        put("mod_version", archiveVersion)
        put("mod_author", authors)
        put("mod_credits", contributors)
        put("mod_license", license)
        put("mod_description", description)
        put("mod_homepage", homepage)
        put("mod_sources", sources)
        put("mod_issues", issues)
        put("mod_author_list", authors.split(", ").joinToString("\",\""))
        put("mod_contributor_list", contributors.split(", ").joinToString("\",\""))
        put("minecraft_range_neoforge", deps.minecraftRangeNeoForge)
        put("minecraft_range_fabric", deps.minecraftRangeFabric)
        put("neoforge_range", deps.minecraftRangeNeoForge)
        prop("deps.neoforge_range") { put("neoforge_range", it) }
    }
}

interface ModCommon {
    val mod: Property<ModData>
}

val extension = project.extensions.create<ModCommon>("mod-common")
extension.mod.convention(ModData())
val mod = extension.mod.get()

configurations.configureEach {
    resolutionStrategy {
        // make sure the desired version of loader is used. Sometimes old versions are pulled in transitively.
        force("net.fabricmc:fabric-loader:${mod.deps.fabricLoader}")
    }
}

tasks {
    named<ProcessResources>("processResources")  {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        fun inputProps(props: Map<String, Any>): Map<String, Any> {
            inputs.properties(*props.map { entry -> entry.key to entry.value }.toTypedArray() )
            return props
        }

        val props = inputProps(mod.getProps())
        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
    }

    named("generateModMetadata") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        description = "Builds the mod, then copies the jar into the root libs folder"
        group = "build"

        from(layout.buildDirectory.dir("libs"))
        include("*.jar")
        into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))

        dependsOn("build")
    }

    register<Delete>("buildCollectAndClean") {
        description = "Builds the mod, then moves the jar into the root libs folder"
        group = "build"

        delete(layout.buildDirectory.dir("libs"))
        delete(layout.buildDirectory.dir("devlibs"))

        dependsOn("buildAndCollect")
    }
}