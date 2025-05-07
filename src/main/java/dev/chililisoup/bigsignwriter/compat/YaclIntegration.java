package dev.chililisoup.bigsignwriter.compat;

import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static dev.chililisoup.bigsignwriter.BigSignWriterConfig.MAIN_CONFIG;

public abstract class YaclIntegration {
    public static Screen generateScreen(Screen parentScreen) {
        BigSignWriterConfig.MainConfig defaults = new BigSignWriterConfig.MainConfig();
        Text name = Text.translatableWithFallback(
                "bigsignwriter.config",
                "Big Sign Writer Config"
        );

        return YetAnotherConfigLib.createBuilder()
                .title(name)
                .category(ConfigCategory.createBuilder()
                        .name(name)
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatableWithFallback(
                                        "bigsignwriter.config.buttonsX",
                                        "Buttons X"))
                                .description(OptionDescription.of(Text.translatableWithFallback(
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
                                        .range(-1000, 1000)
                                        .step(10)
                                        .formatValue(val -> Text.literal(val + "px")))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatableWithFallback(
                                        "bigsignwriter.config.buttonsY",
                                        "Buttons Y"))
                                .description(OptionDescription.of(Text.translatableWithFallback(
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
                                        .range(-1000, 1000)
                                        .step(10)
                                        .formatValue(val -> Text.literal(val + "px")))
                                .build())
                        .option(Option.<String>createBuilder()
                                .name(Text.translatableWithFallback(
                                        "bigsignwriter.config.defaultCharacterSeparator",
                                        "Default Character Separator"))
                                .description(OptionDescription.of(Text.translatableWithFallback(
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
