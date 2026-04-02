package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class IntegerSlider extends AbstractSliderButton {
    private final Consumer<Integer> onChange;
    private final int min;
    private final int max;
    private final int step;
    private final Component label;

    public IntegerSlider(int value, int min, int max, int step, Consumer<Integer> onChange, Component label) {
        super(0, 0, 0, 20, label, (double) (value - min) / (max - min));
        this.onChange = onChange;
        this.min = min;
        this.max = max;
        this.step = step;
        this.label = label;
        this.updateMessage();
    }

    public void setValueFrom(int value) {
        this.setValue((double) (value - this.min) / (this.max - this.min));
    }

    public int getValue() {
        return Mth.roundToward(
                (int) Math.round(this.value * (this.max - this.min) + this.min),
                this.step
        );
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.translatable("options.generic_value", this.label, this.getValue()));
    }

    @Override
    protected void applyValue() {
        this.onChange.accept(this.getValue());
    }
}
