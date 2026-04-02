package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

//? if >= 1.21.9 {
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;
//?}

public class ClickableButtonWidget extends Button/*? if >= 1.21.11 {*/.Plain/*?}*/ {
    public ClickableButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    public void onClick(
            //? if >= 1.21.9 {
            @NotNull MouseButtonEvent mouseButtonEvent, boolean bl
            //?} else
            //double mouseX, double mouseY
    ) {
        this.onPress.onPress(this);
    }

    @Override
    public void onPress(
            //? if >= 1.21.9
            @NotNull InputWithModifiers input
    ) {}

    @Override
    public boolean keyPressed(
            //? if >= 1.21.9 {
            @NotNull KeyEvent keyEvent
            //?} else
            //int keyCode, int scanCode, int modifiers
    ) {
        return false;
    }
}
