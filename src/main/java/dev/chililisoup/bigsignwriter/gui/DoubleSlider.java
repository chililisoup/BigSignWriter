package dev.chililisoup.bigsignwriter.gui;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;

public class DoubleSlider extends AbstractSliderButton {
    private final Consumer<Double> onChange;
    private final double min;
    private final double max;
    private final double step;
    private final Component label;
    private Function<Double, String> valueFormatter = String::valueOf;

    public DoubleSlider(double value, double min, double max, double step, Consumer<Double> onChange, Component label) {
        super(0, 0, 0, 20, label, (value - min) / (max - min));
        this.onChange = onChange;
        this.min = min;
        this.max = max;
        this.step = step;
        this.label = label;
        this.updateMessage();
    }

    public void setValueFormatter(Function<Double, String> valueFormatter) {
        this.valueFormatter = valueFormatter;
        this.updateMessage();
    }

    public void setValueFrom(double value) {
        this.setValue((value - this.min) / (this.max - this.min));
    }

    public double getValue() {
        double value = this.value * (this.max - this.min) + this.min;
        value += this.step / 2.0;
        value -= value % this.step;

        return Math.clamp(value, this.min, this.max);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.translatable("options.generic_value", this.label, this.valueFormatter.apply(this.getValue())));
    }

    @Override
    protected void applyValue() {
        this.onChange.accept(this.getValue());
    }
}
