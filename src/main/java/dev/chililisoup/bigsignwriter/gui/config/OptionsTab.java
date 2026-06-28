package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class OptionsTab extends ConfigTab<OptionsTab.OptionsSidePanel> {
    public OptionsTab(BigSignWriterConfigScreen screen) {
        super(screen, Component.translatable("bigsignwriter.config.options"));
    }

    @Override
    protected Layout buildContent() {
        GridLayout content = new GridLayout().spacing(8);
        GridLayout.RowHelper rowHelper = content.createRowHelper(1);

        BigSignWriterConfig.MainConfig workingConfig = this.screen.workingConfig;
        BigSignWriterConfig.MainConfig defaults = this.screen.defaults;

        rowHelper.addChild(new OptionElement<>(
                screen.workingConfig.buttonsX,
                defaults.buttonsX,
                value -> workingConfig.buttonsX = value,
                option -> new OptionController.IntegerController(option, -500, 500, 5),
                Component.translatable("bigsignwriter.config.buttonsX"),
                Component.translatable("bigsignwriter.config.buttonsX.desc")
        ));
        rowHelper.addChild(new OptionElement<>(
                workingConfig.buttonsY,
                defaults.buttonsY,
                value -> workingConfig.buttonsY = value,
                option -> new OptionController.IntegerController(option, -500, 500, 5),
                Component.translatable("bigsignwriter.config.buttonsY"),
                Component.translatable("bigsignwriter.config.buttonsY.desc")
        ));
        rowHelper.addChild(new OptionElement<>(
                workingConfig.buttonsAlignmentX,
                defaults.buttonsAlignmentX,
                value -> workingConfig.buttonsAlignmentX = value,
                option -> new OptionController.DoubleController(option, 0.0, 1.0, 0.01)
                        .withValueFormatter(value -> (int) Math.round(value * 100) + "%"),
                Component.translatable("bigsignwriter.config.buttonsAlignmentX"),
                Component.translatable("bigsignwriter.config.buttonsAlignmentX.desc")
        ));
        rowHelper.addChild(new OptionElement<>(
                workingConfig.buttonsAlignmentY,
                defaults.buttonsAlignmentY,
                value -> workingConfig.buttonsAlignmentY = value,
                option -> new OptionController.DoubleController(option, 0.0, 1.0, 0.01)
                        .withValueFormatter(value -> (int) Math.round(value * 100) + "%"),
                Component.translatable("bigsignwriter.config.buttonsAlignmentY"),
                Component.translatable("bigsignwriter.config.buttonsAlignmentY.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.fontSelectorCoversDoneButton,
                defaults.fontSelectorCoversDoneButton,
                value -> workingConfig.fontSelectorCoversDoneButton = value,
                Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton"),
                Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.fontSelectorOpensScrolledUp,
                defaults.fontSelectorOpensScrolledUp,
                value -> workingConfig.fontSelectorOpensScrolledUp = value,
                Component.translatable("bigsignwriter.config.fontSelectorOpensScrolledUp"),
                Component.translatable("bigsignwriter.config.fontSelectorOpensScrolledUp.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.showConfigButton,
                defaults.showConfigButton,
                value -> workingConfig.showConfigButton = value,
                Component.translatable("bigsignwriter.config.showConfigButton"),
                Component.translatable("bigsignwriter.config.showConfigButton.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.showSymbolsButton,
                defaults.showSymbolsButton,
                value -> workingConfig.showSymbolsButton = value,
                Component.translatable("bigsignwriter.config.showSymbolsButton"),
                Component.translatable("bigsignwriter.config.showSymbolsButton.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.displayFontHeights,
                defaults.displayFontHeights,
                value -> workingConfig.displayFontHeights = value,
                Component.translatable("bigsignwriter.config.displayFontHeights"),
                Component.translatable("bigsignwriter.config.displayFontHeights.desc")
        ));
        rowHelper.addChild(new OptionElement.BooleanOption(
                workingConfig.characterSeparatorOverrideEnabled,
                defaults.characterSeparatorOverrideEnabled,
                value -> workingConfig.characterSeparatorOverrideEnabled = value,
                Component.translatable("bigsignwriter.config.characterSeparatorOverrideEnabled"),
                Component.translatable("bigsignwriter.config.characterSeparatorOverrideEnabled.desc")
        ));
        rowHelper.addChild(new OptionElement.StringOption(
                workingConfig.characterSeparatorOverride,
                defaults.characterSeparatorOverride,
                value -> workingConfig.characterSeparatorOverride = value,
                Component.translatable("bigsignwriter.config.characterSeparatorOverride"),
                Component.translatable("bigsignwriter.config.characterSeparatorOverride.desc")
        ));
        return content;
    }

    @Override
    protected OptionsTab.OptionsSidePanel buildSidePanel() {
        return new OptionsTab.OptionsSidePanel();
    }

    @Override
    public void arrangeElements(int contentWidth) {
        this.getContent().visitChildren(element -> {
            if (element instanceof OptionElement<?> optionElement)
                optionElement.setWidth(contentWidth);
        });
    }

    protected final class OptionsSidePanel extends ConfigTab.SidePanel {
        @Override
        protected void arrangeSelf() {
            this.height = this.maxHeight;
        }

        @Override
        protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
            AtomicReference<OptionElement<?>> hoveredOption = new AtomicReference<>();
            AtomicReference<OptionElement<?>> focusedOption = new AtomicReference<>();

            OptionsTab.this.getContent().visitChildren(element -> {
                if (element instanceof OptionElement<?> optionElement) {
                    int left = optionElement.getX();
                    int right = left + optionElement.getWidth();
                    int top = optionElement.getY();
                    int bottom = top + optionElement.getHeight();
                    if (mouseX >= left && mouseX < right && mouseY >= top && mouseY < bottom)
                        hoveredOption.set(optionElement);

                    AtomicBoolean focused = new AtomicBoolean(false);
                    optionElement.visitWidgets(widget -> {
                        if (widget.isFocused()) focused.set(true);
                    });
                    if (focused.get()) focusedOption.set(optionElement);
                }
            });

            OptionElement<?> optionElement = hoveredOption.get();
            if (optionElement == null) optionElement = focusedOption.get();
            if (optionElement != null) {
                GraphicsHelper.drawScrollingString(
                        guiGraphics,
                        optionElement.name.copy().withStyle(ChatFormatting.BOLD),
                        this.getX(),
                        this.getRight(),
                        this.getY(),
                        this.getY() + 10
                );

                guiGraphics.fill(this.getX(), this.getY() + 15, this.getRight(), this.getY() + 16, 0xFFFFFFFF);

                guiGraphics.textWithWordWrap(
                        OptionsTab.this.screen.font,
                        optionElement.description,
                        this.getX(),
                        this.getY() + 25,
                        this.width,
                        -1
                );
            }
        }
    }
}
