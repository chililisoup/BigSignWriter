package dev.chililisoup.bigsignwriter.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class FontFile {
    private static final Comparator<Character> COMPARATOR = FontFile::compare;

    public String name;
    public @Nullable String credits;
    public int height = 4;
    public @Nullable String characterSeparator;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, @NotNull String credits, Map<Character, String[]> characters) {
        this.name = name;
        this.credits = credits;
        this.characters = new TreeMap<>(COMPARATOR);
        this.characters.putAll(characters);
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

    private static int compare(char a, char b) {
        if (a == b) return 0;
        boolean aOrdered = isOrdered(a);
        boolean bOrdered = isOrdered(b);
        if (aOrdered != bOrdered) return aOrdered ? -1 : 1;
        return aOrdered ? orderOf(a) - orderOf(b) : a - b;
    }

    private static boolean isOrdered(char chr) {
        if (chr >= 97) return chr <= 122; // a-z
        if (chr >= 65) return chr <= 90; // A-Z
        return chr >= 48 && chr <= 57; // 0-9
    }

    private static int orderOf(char chr) {
        if (chr >= 97) return chr - 71; // a-z -> 26-51
        return chr - (chr >= 65 ? 65 : -4); // A-Z -> 0-25, 0-9 -> 52-61
    }
}
