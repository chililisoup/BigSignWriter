package dev.chililisoup.bigsignwriter;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

//? if >= 1.21.9 {
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?}

public class ClickableButtonWidget extends Button {
    public ClickableButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    //? if < 1.21.9 {
    /*public void onClick(double mouseX, double mouseY) {
    *///?} else
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean bl) {
        this.onPress.onPress(this);
    }

    @Override
    //? if < 1.21.9 {
    /*public void onPress() {}
    *///?} else
    public void onPress(InputWithModifiers inputWithModifiers) {}

    @Override
    //? if < 1.21.9 {
    /*public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    *///?} else
    public boolean keyPressed(KeyEvent keyEvent) {
        return false;
    }
}
