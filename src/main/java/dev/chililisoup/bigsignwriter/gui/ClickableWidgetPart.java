package dev.chililisoup.bigsignwriter.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class ClickableWidgetPart {
    protected final @Nullable Component tooltip;
    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;
    protected final Runnable onClick;
    public boolean visible = true;

    public ClickableWidgetPart(@Nullable Component tooltip, int x, int y, int width, int height, Runnable onClick) {
        this.tooltip = tooltip;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent) {
        if (!this.visible || !this.hovered(mouseButtonEvent))
            return false;

        AbstractWidget.playButtonClickSound(Minecraft.getInstance().getSoundManager());
        this.onClick.run();
        return true;
    }

    public boolean hovered(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width
                && mouseY >= this.y && mouseY < this.y + this.height;
    }

    public boolean hovered(MouseButtonEvent mouseButtonEvent) {
        return this.hovered((int) mouseButtonEvent.x(), (int) mouseButtonEvent.y());
    }

    public final void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        if (!this.visible) return;

        boolean hovered = this.hovered(mouseX, mouseY);
        if (hovered) {
            guiGraphics.requestCursor(CursorTypes.POINTING_HAND);
            if (this.tooltip != null) guiGraphics.setTooltipForNextFrame(this.tooltip, mouseX, mouseY);
        }
        this.extractWidgetPartRenderState(guiGraphics, hovered);
    }

    protected abstract void extractWidgetPartRenderState(GuiGraphicsExtractor guiGraphics, boolean hovered);
}
