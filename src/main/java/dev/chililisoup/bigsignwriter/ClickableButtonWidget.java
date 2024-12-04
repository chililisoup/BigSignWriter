package dev.chililisoup.bigsignwriter;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickableButtonWidget extends ButtonWidget {
    public ClickableButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
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
