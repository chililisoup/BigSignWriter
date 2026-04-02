package dev.chililisoup.bigsignwriter.font;

import java.util.Map;
import java.util.TreeMap;

public class FontFile {
    public String name;
    public int height = 4;
    public String characterSeparator;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, Map<Character, String[]> characters) {
        this.name = name;
        this.characters = new TreeMap<>(characters);
    }

    public FontFile(String name, int height, Map<Character, String[]> characters) {
        this(name, characters);
        this.height = height;
    }

    public FontFile(String name, String characterSeparator, Map<Character, String[]> characters) {
        this(name, characters);
        this.characterSeparator = characterSeparator;
    }

    public FontFile(String name, int height, String characterSeparator, Map<Character, String[]> characters) {
        this(name, height, characters);
        this.characterSeparator = characterSeparator;
    }

    public final FontInfo createInfo() {
        return new FontInfo(this);
    }
}
