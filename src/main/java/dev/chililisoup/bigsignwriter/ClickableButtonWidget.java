package dev.chililisoup.bigsignwriter;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ClickableButtonWidget extends Button {
    public ClickableButtonWidget(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress.onPress(this);
    }

    @Override
    public void onPress() {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
}
