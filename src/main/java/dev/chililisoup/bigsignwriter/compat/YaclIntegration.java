package dev.chililisoup.bigsignwriter.compat;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.MAIN_CONFIG;

public abstract class YaclIntegration {
    public static Screen generateScreen(Screen parentScreen) {
        BigSignWriterConfig.MainConfig defaults = new BigSignWriterConfig.MainConfig();
        Component name = Component.translatable("bigsignwriter.config");

        return YetAnotherConfigLib.createBuilder()
                .title(name)
                .category(ConfigCategory.createBuilder()
                        .name(name)
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.buttonsX"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.buttonsX.desc")))
                                .binding(
                                        defaults.buttonsX,
                                        () -> MAIN_CONFIG.buttonsX,
                                        newVal -> {
                                            MAIN_CONFIG.buttonsX = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-500, 500)
                                        .step(5)
                                        .formatValue(val -> Component.literal(val + "px")))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.buttonsY"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.buttonsY.desc")))
                                .binding(
                                        defaults.buttonsY,
                                        () -> MAIN_CONFIG.buttonsY,
                                        newVal -> {
                                            MAIN_CONFIG.buttonsY = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-500, 500)
                                        .step(5)
                                        .formatValue(val -> Component.literal(val + "px")))
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.buttonsAlignmentX"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.buttonsAlignmentX.desc")))
                                .binding(
                                        defaults.buttonsAlignmentX,
                                        () -> MAIN_CONFIG.buttonsAlignmentX,
                                        newVal -> {
                                            MAIN_CONFIG.buttonsAlignmentX = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.0, 1.0)
                                        .step(0.05)
                                        .formatValue(val -> Component.literal((int) (val * 100) + "%")))
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.buttonsAlignmentY"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.buttonsAlignmentY.desc")))
                                .binding(
                                        defaults.buttonsAlignmentY,
                                        () -> MAIN_CONFIG.buttonsAlignmentY,
                                        newVal -> {
                                            MAIN_CONFIG.buttonsAlignmentY = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.0, 1.0)
                                        .step(0.05)
                                        .formatValue(val -> Component.literal((int) (val * 100) + "%")))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.fontSelectorCoversDoneButton.desc")))
                                .binding(
                                        defaults.fontSelectorCoversDoneButton,
                                        () -> MAIN_CONFIG.fontSelectorCoversDoneButton,
                                        newVal -> {
                                            MAIN_CONFIG.fontSelectorCoversDoneButton = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.showReloadButton"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.showReloadButton.desc")))
                                .binding(
                                        defaults.showReloadButton,
                                        () -> MAIN_CONFIG.showReloadButton,
                                        newVal -> {
                                            MAIN_CONFIG.showReloadButton = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable("bigsignwriter.config.defaultCharacterSeparator"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.config.defaultCharacterSeparator.desc")))
                                .binding(
                                        defaults.defaultCharacterSeparator,
                                        () -> MAIN_CONFIG.defaultCharacterSeparator,
                                        newVal -> {
                                            MAIN_CONFIG.defaultCharacterSeparator = newVal;
                                            BigSignWriterConfig.saveConfig();
                                        })
                                .controller(StringControllerBuilder::create)
                                .build())
                        .build())

                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("bigsignwriter.debug"))
                        .option(ButtonOption.createBuilder()
                                .name(Component.translatable("bigsignwriter.debug.validateFonts"))
                                .description(OptionDescription.of(Component.translatable("bigsignwriter.debug.validateFonts.desc")))
                                .action(((yaclScreen, buttonOption) -> BigSignWriter.analyzeFonts()))
                                .build())
                        .build())
                .build()
                .generateScreen(parentScreen);
    }
}
