package dev.chililisoup.bigsignwriter.font;

import java.util.Map;

public class FontFile {
    public String name;
    public String characterSeparator;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, Map<Character, String[]> characters) {
        this.name = name;
        this.characters = characters;
    }

    public FontFile(String name, String characterSeparator, Map<Character, String[]> characters) {
        this(name, characters);
        this.characterSeparator = characterSeparator;
    }
}
