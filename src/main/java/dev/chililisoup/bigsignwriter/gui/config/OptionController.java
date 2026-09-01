package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.config.ConfigurableEnum;
import dev.chililisoup.bigsignwriter.gui.DoubleSlider;
import dev.chililisoup.bigsignwriter.gui.IntegerSlider;
import dev.chililisoup.bigsignwriter.gui.LabeledEditBox;
import dev.chililisoup.bigsignwriter.gui.TickBox;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public interface OptionController<T> {
    void setValue(OptionElement<T> option);

    AbstractWidget widget();

    record BooleanController(TickBox tickBox) implements OptionController<Boolean> {
        public BooleanController(OptionElement<Boolean> option) {
            this(new TickBox(option.value, option::setValue, option.name));
        }

        @Override
        public void setValue(OptionElement<Boolean> option) {
            this.tickBox.value = option.value;
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
        public void setValue(OptionElement<Integer> option) {
            this.slider.setValueFrom(option.value);
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
        public void setValue(OptionElement<Double> option) {
            this.slider.setValueFrom(option.value);
        }

        @Override
        public AbstractWidget widget() {
            return this.slider;
        }
    }

    record StringController(LabeledEditBox editBox) implements OptionController<String> {
        public StringController(OptionElement<String> option, int width) {
            this(new LabeledEditBox(option.value, width, option::setValue, option.name));
        }

        @Override
        public void setValue(OptionElement<String> option) {
            this.editBox.setValueSilent(option.value);
        }

        @Override
        public AbstractWidget widget() {
            return this.editBox;
        }
    }

    record EnumController<T extends ConfigurableEnum<T>>(Button button) implements OptionController<T> {
        public EnumController(OptionElement<T> option) {
            this(Button.builder(
                    createButtonMessage(option),
                    button -> option.setValue(option.value.next())
            ).build());
        }

        private static<T extends ConfigurableEnum<T>> Component createButtonMessage(OptionElement<T> option) {
            return CommonComponents.optionNameValue(option.name, option.value.valueName());
        }

        @Override
        public void setValue(OptionElement<T> option) {
            this.button.setMessage(createButtonMessage(option));
        }

        @Override
        public AbstractWidget widget() {
            return this.button;
        }
    }
}
