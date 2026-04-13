package dev.chililisoup.bigsignwriter.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class FontFile {
    public static final Comparator<Character> COMPARATOR = FontFile::compare;

    public String name;
    public @Nullable String credits = null;
    public int height = 4;
    public @Nullable String characterSeparator = null;
    public @Nullable String parentFont = null;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, @NotNull String credits) {
        this.name = name;
        this.credits = credits;
    }

    public FontFile height(int height) {
        this.height = height;
        return this;
    }

    public FontFile characterSeparator(String characterSeparator) {
        this.characterSeparator = characterSeparator;
        return this;
    }

    public FontFile parentFont(String parentFont) {
        this.parentFont = parentFont;
        return this;
    }

    @SafeVarargs
    public final FontFile characters(Map.Entry<Character, String[]>... entries) {
        this.characters = new TreeMap<>(COMPARATOR);
        this.characters.putAll(Map.ofEntries(entries));
        return this;
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
