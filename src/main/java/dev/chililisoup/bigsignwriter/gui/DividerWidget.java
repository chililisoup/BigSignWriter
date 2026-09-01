package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public class DividerWidget extends AbstractWidget {
    private final int padding;

    public DividerWidget(int x, int y, int width, int height, int padding) {
        super(x, y, width, height, CommonComponents.EMPTY);
        this.padding = padding;
    }

    public DividerWidget(int padding) {
        this(0, 0, 150, 20, padding);
    }

    public DividerWidget() {
        this(10);
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.horizontalLine(
                this.getX() + this.padding,
                this.getRight() - 1 - this.padding,
                this.getY() + this.getHeight() / 2,
                -1
        );
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        return false;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {}
}
