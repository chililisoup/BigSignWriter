package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ClickableButtonWidget extends
        //~ if >= 1.21.11 'Button' -> 'Button.Plain'
        Button.Plain
{
    public ClickableButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean doubleClick) {
        this.onPress.onPress(this);
    }

    @Override
    public void onPress(@NotNull InputWithModifiers input) {}

    @Override
    public boolean keyPressed(@NotNull KeyEvent event) {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {}
}
