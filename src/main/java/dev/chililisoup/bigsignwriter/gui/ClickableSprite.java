package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class ClickableSprite extends ClickableWidgetPart {
    private final Identifier sprite;
    private final int lineLeft;
    private final int lineRight;
    private final int lineY;

    public ClickableSprite(Identifier sprite, @Nullable Component tooltip, int x, int y, int width, int height, int lineLeft, int lineRight, int lineY, Runnable onClick) {
        super(tooltip, x, y, width, height, onClick);
        this.sprite = sprite;
        this.lineLeft = lineLeft;
        this.lineRight = lineRight - 1;
        this.lineY = lineY;
    }


    public ClickableSprite(Identifier sprite, @Nullable Component tooltip, int x, int y, int width, int height, Runnable onClick) {
        this(sprite, tooltip, x, y, width, height, x, x + width, y + height, onClick);
    }

    @Override
    protected void extractWidgetPartRenderState(GuiGraphicsExtractor guiGraphics, boolean hovered) {
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                this.sprite,
                this.x,
                this.y,
                this.width,
                this.height
        );
        if (hovered) guiGraphics.horizontalLine(this.lineLeft, this.lineRight, this.lineY, -1);
    }
}
