package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

//? if >= 1.21.11 {
public abstract class IconButton extends Button.Plain {
//?} else
//public abstract class IconButton extends Button {
    public IconButton(OnPress onPress) {
        super(0, 0, 20, 20, Component.empty(), onPress, Button.DEFAULT_NARRATION);
    }

    protected abstract Identifier getSprite();

    @Override
    //? if >= 1.21.11 {
    protected void extractContents(
    //?} else
    //protected void extractWidgetRenderState(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        //? if >= 26.1 {
        super.extractContents(
        //?} else
        //super.extractWidgetRenderState(
                guiGraphics, mouseX, mouseY, partialTick
        );

        guiGraphics.blitSprite(
                //? if >= 1.21.3
                RenderPipelines.GUI_TEXTURED,
                this.getSprite(),
                this.getX() + 2,
                this.getY() + 2,
                16,
                16
        );
    }
}
