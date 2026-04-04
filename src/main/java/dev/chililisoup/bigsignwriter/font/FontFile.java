package dev.chililisoup.bigsignwriter.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class FontFile {
    public String name;
    public @Nullable String credits;
    public int height = 4;
    public @Nullable String characterSeparator;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, @NotNull String credits, Map<Character, String[]> characters) {
        this.name = name;
        this.credits = credits;
        this.characters = new TreeMap<>(characters);
    }

    public FontFile(String name, String credits, int height, Map<Character, String[]> characters) {
        this(name, credits, characters);
        this.height = height;
    }

    public FontFile(String name, String credits, @NotNull String characterSeparator, Map<Character, String[]> characters) {
        this(name, credits, characters);
        this.characterSeparator = characterSeparator;
    }

    public FontFile(String name, String credits, int height, @NotNull String characterSeparator, Map<Character, String[]> characters) {
        this(name, credits, height, characters);
        this.characterSeparator = characterSeparator;
    }

    public final FontInfo createInfo(String source) {
        return new FontInfo(this, source);
    }
}
