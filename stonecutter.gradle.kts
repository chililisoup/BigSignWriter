plugins {
    id("dev.kikugie.stonecutter")

    val modstitchVersion = "0.8.5"
    id("dev.isxander.modstitch.base") version modstitchVersion apply false
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT" apply false
}

stonecutter active "26.1-fabric"

stonecutter parameters {
    replacements {
        string(current.parsed >= "1.21.6") {
            replace("RenderType::guiTextured", "RenderPipelines.GUI_TEXTURED")
            replace("net.minecraft.client.renderer.RenderType", "net.minecraft.client.renderer.RenderPipelines")
            replace("pushPose", "pushMatrix")
            replace("popPose", "popMatrix")
            replace("guiGraphics.renderTooltip(", "guiGraphics.setTooltipForNextFrame(")
        }

        string(current.parsed > "1.21.6") {
            replace(
                "dev.chililisoup.bigsignwriter.util.VersionHelper.KeyEvent",
                "net.minecraft.client.input.KeyEvent"
            )
        }

        string(current.parsed >= "1.21.9" && current.parsed < "1.21.11") {
            replace("guiGraphics.renderOutline(", "guiGraphics.submitOutline(")
        }

        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            replace(".location()", ".identifier()")
        }

        string(current.parsed >= "26.1") {
            replace("GuiGraphics", "GuiGraphicsExtractor")
            replace("guiGraphics.drawString(", "guiGraphics.text(")
            replace("guiGraphics.drawWordWrap(", "guiGraphics.textWithWordWrap(")
            replace("guiGraphics.renderOutline(", "guiGraphics.outline(")

            replace("renderContents", "extractContents")
            replace("renderWidget", "extractWidgetRenderState")
            replace("renderMenuBackground", "extractMenuBackground")
            replace("renderMenuBackgroundTexture", "extractMenuBackgroundTexture")
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
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