package dev.chililisoup.bigsignwriter.config;

public enum SymbolColoringMode implements ConfigurableEnum<SymbolColoringMode> {
    WHILE_HOLDING_SHIFT("while_holding_shift"),
    ALWAYS("always"),
    NEVER("never");

    private final String languageKey;

    SymbolColoringMode(String languageKey) {
        this.languageKey = "bigsignwriter.config.symbolColoringMode." + languageKey;
    }

    @Override
    public String languageKey() {
        return this.languageKey;
    }

    @Override
    public SymbolColoringMode next() {
        SymbolColoringMode[] entries = values();
        return entries[(this.ordinal() + 1) % entries.length];
    }
}
