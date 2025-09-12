package dev.chililisoup.bigsignwriter.compat;

import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.MAIN_CONFIG;

public abstract class YaclIntegration {
    public static Screen generateScreen(Screen parentScreen) {
        BigSignWriterConfig.MainConfig defaults = new BigSignWriterConfig.MainConfig();
        Component name = Component.translatableWithFallback(
                "bigsignwriter.config",
                "Big Sign Writer Config"
        );

        return YetAnotherConfigLib.createBuilder()
                .title(name)
                .category(ConfigCategory.createBuilder()
                        .name(name)
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsX",
                                        "Buttons X"))
                                .description(OptionDescription.of(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsX.desc",
                                        "Horizontal offset for mod buttons, from center of screen.")))
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
                                .name(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsY",
                                        "Buttons Y"))
                                .description(OptionDescription.of(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsY.desc",
                                        "Vertical offset for mod buttons, from center of screen.")))
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
                                .name(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsAlignmentX",
                                        "Buttons Alignment X"))
                                .description(OptionDescription.of(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsAlignmentX.desc",
                                        "Horizontal screen alignment for mod buttons (0%: left, 100%: right)")))
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
                                .name(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsAlignmentY",
                                        "Buttons Alignment Y"))
                                .description(OptionDescription.of(Component.translatableWithFallback(
                                        "bigsignwriter.config.buttonsAlignmentY.desc",
                                        "Vertical screen alignment for mod buttons (0%: top, 100%: bottom)")))
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
                        .option(Option.<String>createBuilder()
                                .name(Component.translatableWithFallback(
                                        "bigsignwriter.config.defaultCharacterSeparator",
                                        "Default Character Separator"))
                                .description(OptionDescription.of(Component.translatableWithFallback(
                                        "bigsignwriter.config.defaultCharacterSeparator.desc",
                                        "Default separator string to place between characters. May be overridden by fonts.")))
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
                .build()
                .generateScreen(parentScreen);
    }
}
