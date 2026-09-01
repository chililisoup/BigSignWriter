package dev.chililisoup.bigsignwriter.font;

import java.util.Optional;

public record SymbolReference(String key, FontInfo sourceFont) {
    public String[] get() {
        return Optional.ofNullable(this.sourceFont.symbols().get(this.key)).orElse(new String[0]);
    }

    public int height() {
        return this.get().length;
    }

    public String characterSeparator() {
        return this.sourceFont.characterSeparator();
    }

    public String expandedId() {
        return this.sourceFont.id + ":" + this.key;
    }
}
