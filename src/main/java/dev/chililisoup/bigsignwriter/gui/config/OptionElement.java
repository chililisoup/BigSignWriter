package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.AbstractLayoutElement;
import dev.chililisoup.bigsignwriter.gui.IconButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class OptionElement<T> extends AbstractLayoutElement {
    T value;
    private final T defaultValue;
    private final Consumer<T> onChange;
    private final OptionController<T> controller;
    private final ResetButton resetButton;
    final Component name;
    final Component description;

    OptionElement(
            T value,
            T defaultValue,
            Consumer<T> onChange,
            Function<OptionElement<T>, OptionController<T>> controllerBuilder,
            Component name,
            Component description
    ) {
        this.value = value;
        this.defaultValue = defaultValue;
        this.onChange = onChange;
        this.resetButton = new ResetButton(button -> this.setValue(this.defaultValue));
        this.resetButton.active = !defaultValue.equals(value);
        this.name = name;
        this.description = description;
        this.controller = controllerBuilder.apply(this);
    }

    public void setValue(T value) {
        this.value = value;
        this.resetButton.active = !defaultValue.equals(value);
        this.controller.setValue(value);
        this.onChange.accept(value);
    }

    @Override
    protected void arrangeElements() {
        this.controller.widget().setPosition(this.x, this.y);
        this.controller.widget().setWidth(this.width - 22);
        this.resetButton.setPosition(this.x + this.width - 20, this.y);
    }

    @Override
    public void visitWidgets(@NotNull Consumer<AbstractWidget> widgetVisitor) {
        widgetVisitor.accept(this.controller.widget());
        widgetVisitor.accept(this.resetButton);
    }

    private static class ResetButton extends IconButton {
        private static final Identifier RESET_SPRITE = BigSignWriter.id("reset");

        public ResetButton(OnPress onPress) {
            super(onPress);
        }

        @Override
        protected Identifier getSprite() {
            return RESET_SPRITE;
        }
    }
}
