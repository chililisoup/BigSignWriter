package dev.chililisoup.bigsignwriter.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class FontFile {
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
        this.parentFont = "builtin/" + parentFont;
        return this;
    }

    @SafeVarargs
    public final FontFile characters(Map.Entry<Character, String[]>... entries) {
        this.characters = new TreeMap<>(FontFile::compareChars);
        this.characters.putAll(Map.ofEntries(entries));
        return this;
    }

    public FontFile copyWithUnsafeCharacters() {
        FontFile copy = new FontFile();

        copy.name = this.name;
        copy.credits = this.credits;
        copy.height = height;
        copy.characterSeparator = this.characterSeparator;
        copy.parentFont = this.parentFont;
        copy.characters = this.characters;

        return copy;
    }

    public static int compareChars(char a, char b) {
        if (a == b) return 0;
        boolean aOrdered = charHasSetOrder(a);
        boolean bOrdered = charHasSetOrder(b);
        if (aOrdered != bOrdered) return aOrdered ? -1 : 1;
        return aOrdered ? getSetCharOrder(a) - getSetCharOrder(b) : a - b;
    }

    private static boolean charHasSetOrder(char chr) {
        if (chr >= 97) return chr <= 122; // a-z
        if (chr >= 65) return chr <= 90; // A-Z
        return chr >= 48 && chr <= 57; // 0-9
    }

    private static int getSetCharOrder(char chr) {
        if (chr >= 97) return chr - 71; // a-z -> 26-51
        return chr - (chr >= 65 ? 65 : -4); // A-Z -> 0-25, 0-9 -> 52-61
    }
}
