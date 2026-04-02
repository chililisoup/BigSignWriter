package dev.chililisoup.bigsignwriter.gui;

import dev.chililisoup.bigsignwriter.util.VersionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class LabeledEditBox extends EditBox {
    private final int boxWidth;

    public LabeledEditBox(String value, int boxWidth, Consumer<String> onChange, Component label) {
        super(Minecraft.getInstance().font, 0, 20, label);
        this.boxWidth = boxWidth;
        this.setValue(value);
        this.setResponder(onChange);
    }

    public void setValueSilent(String value) {
        this.value = value;
        //? if >= 26.1 {
        this.moveCursorToEnd(false);
        //?} else
        //this.setCursorPosition(this.value.length());
    }

    @Override
    public int getInnerWidth() {
        return this.isBordered() ? this.boxWidth - 8 : this.boxWidth;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int width = this.width;
        this.width = this.boxWidth;
        super.extractWidgetRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.width = width;

        int labelX = this.getX() + this.boxWidth + 2;
        Screen.extractMenuBackgroundTexture(
                guiGraphics, Screen.MENU_BACKGROUND, labelX, this.getY(), 0.0F, 0.0F, this.getRight() - labelX, this.getHeight()
        );
        if (this.isHovered()) guiGraphics.fill(
                labelX,
                this.getY(),
                this.getRight(),
                this.getBottom(),
                0x40FFFFFF
        );

        VersionHelper.drawScrollingString(
                guiGraphics,
                this.getMessage(),
                labelX + 2,
                labelX + 2,
                this.getRight() - 2,
                this.getY(),
                this.getBottom()
        );
    }
}
