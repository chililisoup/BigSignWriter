package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.gui.DoubleSlider;
import dev.chililisoup.bigsignwriter.gui.IntegerSlider;
import dev.chililisoup.bigsignwriter.gui.LabeledEditBox;
import dev.chililisoup.bigsignwriter.gui.TickBox;
import net.minecraft.client.gui.components.AbstractWidget;

import java.util.function.Function;

public interface OptionController<T> {
    void setValue(T value);

    AbstractWidget widget();

    record BooleanController(TickBox tickBox) implements OptionController<Boolean> {
        public BooleanController(OptionElement<Boolean> option) {
            this(new TickBox(option.value, option::setValue, option.name));
        }

        @Override
        public void setValue(Boolean value) {
            this.tickBox.value = value;
        }

        @Override
        public AbstractWidget widget() {
            return this.tickBox;
        }
    }

    record IntegerController(IntegerSlider slider) implements OptionController<Integer> {
        public IntegerController(OptionElement<Integer> option, int min, int max, int step) {
            this(new IntegerSlider(option.value, min, max, step, option::setValue, option.name));
        }

        @Override
        public void setValue(Integer value) {
            this.slider.setValueFrom(value);
        }

        @Override
        public AbstractWidget widget() {
            return this.slider;
        }
    }

    record DoubleController(DoubleSlider slider) implements OptionController<Double> {
        public DoubleController(OptionElement<Double> option, double min, double max, double step) {
            this(new DoubleSlider(option.value, min, max, step, option::setValue, option.name));
        }

        public DoubleController withValueFormatter(Function<Double, String> valueFormatter) {
            this.slider.setValueFormatter(valueFormatter);
            return this;
        }

        @Override
        public void setValue(Double value) {
            this.slider.setValueFrom(value);
        }

        @Override
        public AbstractWidget widget() {
            return this.slider;
        }
    }

    record StringController(LabeledEditBox editBox) implements OptionController<String> {
        public StringController(OptionElement<String> option) {
            this(new LabeledEditBox(option.value, 20, option::setValue, option.name));
        }

        @Override
        public void setValue(String value) {
            this.editBox.setValueSilent(value);
        }

        @Override
        public AbstractWidget widget() {
            return this.editBox;
        }
    }
}
