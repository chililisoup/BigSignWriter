package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Map;

public class FontFile {
    public String name;
    public int height = 4;
    public String characterSeparator;
    public Map<Character, String[]> characters;

    public FontFile() {}

    public FontFile(String name, Map<Character, String[]> characters) {
        this.name = name;
        this.characters = characters;
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

    public final Component[] getPreview(String text) {
        return getFontPreview(this, text);
    }

    public final Component[] getPreview() {
        return this.getPreview(this.name);
    }

    private static Component[] getFontPreview(FontFile fontFile, String text) {
        int height = fontFile.height > 0 ? fontFile.height : 4;
        ArrayList<ArrayList<String>> lines = new ArrayList<>(height);
        for (int i = 0; i < height; i++) lines.add(new ArrayList<>());

        for (char chr : text.toCharArray()) {
            String[] bigChar = BigSignWriter.getBigChar(chr, fontFile).orElse(new String[]{""});
            for (int i = 0; i < bigChar.length; i++)
                lines.get(i).add(bigChar[i]);
        }

        if (lines.isEmpty()) return new Component[0];

        Component[] preview = new Component[height];
        String characterSeparator = BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverrideEnabled ?
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride :
                (fontFile.characterSeparator != null ? fontFile.characterSeparator : " ");
        for (int i = 0; i < lines.size(); i++)
            preview[i] = Component.literal(String.join(characterSeparator, lines.get(i)));

        return preview;
    }
}
