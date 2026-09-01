package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public abstract class IconButton extends
        //~ if >= 1.21.11 'Button' -> 'Button.Plain'
        Button.Plain
{
    public IconButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
    }

    public IconButton(int x, int y, OnPress onPress) {
        this(x, y, 20, 20, onPress);
    }

    public IconButton(OnPress onPress) {
        this(0, 0, onPress);
    }

    public static IconButton basic(int x, int y, int width, int height, Identifier sprite, OnPress onPress) {
        return new IconButton(x, y, width, height, onPress) {
            @Override
            protected Identifier getSprite() {
                return sprite;
            }
        };
    }

    public static IconButton basic(int x, int y, Identifier sprite, OnPress onPress) {
        return basic(x, y, 20, 20, sprite, onPress);
    }

    public static IconButton basic(Identifier sprite, OnPress onPress) {
        return basic(0, 0, sprite, onPress);
    }

    protected abstract Identifier getSprite();

    @Override
    //? if >= 1.21.11 {
    protected void extractContents(
    //?} else
    //protected void extractWidgetRenderState(
            @NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick
    ) {
        //? if >= 1.21.11 {
        super.extractContents(
        //?} else
        //super.extractWidgetRenderState(
                guiGraphics, mouseX, mouseY, partialTick
        );

        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                this.getSprite(),
                this.getX() + 2,
                this.getY() + 2,
                16,
                16
        );
    }
}
