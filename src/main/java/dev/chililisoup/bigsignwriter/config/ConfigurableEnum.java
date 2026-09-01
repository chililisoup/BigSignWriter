package dev.chililisoup.bigsignwriter.config;

import net.minecraft.network.chat.Component;

public interface ConfigurableEnum<E> {
    E next();

    String languageKey();

    default Component valueName() {
        return Component.translatable(this.languageKey());
    }
}
