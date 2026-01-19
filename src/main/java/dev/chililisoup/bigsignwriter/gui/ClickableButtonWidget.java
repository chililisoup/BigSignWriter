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
    //? if < 1.21.9 {
    /*public void onClick(double mouseX, double mouseY) {
    *///?} else
    public void onClick(@NotNull MouseButtonEvent mouseButtonEvent, boolean bl) {
        this.onPress.onPress(this);
    }

    @Override
    //? if < 1.21.9 {
    /*public void onPress() {}
    *///?} else
    public void onPress(@NotNull InputWithModifiers inputWithModifiers) {}

    @Override
    //? if < 1.21.9 {
    /*public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    *///?} else
    public boolean keyPressed(@NotNull KeyEvent keyEvent) {
        return false;
    }
}
