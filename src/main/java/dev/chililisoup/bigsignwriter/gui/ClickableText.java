package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ClickableText extends ClickableWidgetPart {
    private final Component message;
    private final int xPadding;

    public ClickableText(Component message, @Nullable Component tooltip, int x, int y, int xPadding, Runnable onPress) {
        super(
                tooltip,
                x - xPadding,
                y,
                Minecraft.getInstance().font.width(message) + xPadding * 2,
                10,
                onPress
        );
        this.message = message;
        this.xPadding = xPadding;
    }

    public ClickableText(Component message, int x, int y, Runnable onPress) {
        this(message, null, x, y, 0, onPress);
    }

    public static ClickableText centered(Component message, int x, int y, Runnable onPress) {
        int width = Minecraft.getInstance().font.width(message);
        return new ClickableText(message, x - width / 2, y, onPress);
    }

    @Override
    protected void extractWidgetPartRenderState(GuiGraphicsExtractor guiGraphics, boolean hovered) {
        guiGraphics.text(
                Minecraft.getInstance().font,
                hovered ?
                        this.message.copy().withStyle(ChatFormatting.UNDERLINE) :
                        this.message,
                this.x + xPadding,
                this.y,
                -1
        );
    }
}
