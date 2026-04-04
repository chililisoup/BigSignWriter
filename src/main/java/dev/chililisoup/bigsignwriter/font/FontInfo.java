package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class FontInfo {
    public final FontFile fontFile;
    public final String source;
    private boolean checked = false;
    private @Nullable Component error = null;

    FontInfo(FontFile fontFile, String source) {
        this.fontFile = fontFile;
        this.source = source;
    }

    public String name() {
        return this.fontFile.name;
    }

    public @Nullable String credits() {
        return this.fontFile.credits;
    }

    public int height() {
        return this.fontFile.height > 0 ? this.fontFile.height : 4;
    }

    public String characterSeparator() {
        return BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverrideEnabled ?
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride :
                (this.fontFile.characterSeparator != null ? this.fontFile.characterSeparator : " ");
    }

    public Map<Character, String[]> characters() {
        return this.fontFile.characters;
    }

    public boolean isBroken() {
        return this.error() != null;
    }

    public boolean isWorking() {
        return !this.isBroken();
    }

    public boolean isBuiltIn() {
        return this.getBuiltInName() != null;
    }

    public boolean isVisible(BigSignWriterConfig.PersistentConfig config) {
        return !config.hiddenFonts.contains(this.source);
    }

    public boolean isVisible() {
        return this.isVisible(BigSignWriterConfig.MAIN_CONFIG);
    }

    public void setVisible(BigSignWriterConfig.PersistentConfig config, boolean visible) {
        if (visible) config.hiddenFonts.remove(this.source);
        else config.hiddenFonts.add(this.source);
    }

    public @Nullable String getBuiltInName() {
        String[] fontSource = this.source.split("/");
        return fontSource.length == 2 && fontSource[0].equals("builtin") ?
                fontSource[1] : null;
    }

    public @Nullable Component error() {
        if (this.checked) return this.error;
        this.error = getError(fontFile);
        this.checked = true;
        return this.error;
    }

    private static @Nullable Component getError(FontFile fontFile) {
        if (fontFile.height <= 0) return Component.translatable(
                "bigsignwriter.font.error.invalidHeight",
                fontFile.height
        );

        Font font = Minecraft.getInstance().font;
        for (Map.Entry<Character, String[]> entry : fontFile.characters.entrySet()) {
            char baseChar = entry.getKey();
            String[] bigChar = entry.getValue();

            if (bigChar.length != fontFile.height) return Component.translatable(
                    "bigsignwriter.font.error.wrongLineCount",
                    baseChar,
                    bigChar.length,
                    fontFile.height
            );

            int[] widths = new int[bigChar.length];
            widths[0] = font.width(bigChar[0]);
            boolean unfixed = false;
            for (int i = 1; i < bigChar.length; i++) {
                widths[i] = font.width(bigChar[i]);
                if (widths[i] != widths[0]) unfixed = true;
            }
            if (unfixed) return Component.translatable(
                    "bigsignwriter.font.error.unfixedWidth",
                    baseChar,
                    Arrays.toString(widths)
            );
        }

        return null;
    }

    public final Component[] getPreview(String text, String characterSeparator) {
        return getFontPreview(this, text, characterSeparator);
    }

    public final Component[] getPreview(String text) {
        return this.getPreview(text, this.characterSeparator());
    }

    public final Component[] getPreview() {
        return this.getPreview(this.name());
    }

    private static Component[] getFontPreview(FontInfo fontInfo, String text, String characterSeparator) {
        int height = fontInfo.height();
        ArrayList<ArrayList<String>> lines = new ArrayList<>(height);
        for (int i = 0; i < height; i++) lines.add(new ArrayList<>());

        for (char chr : text.toCharArray()) {
            String[] bigChar = BigSignWriter.getBigChar(chr, fontInfo).orElse(new String[]{""});
            int length = Math.min(height, bigChar.length);
            for (int i = 0; i < length; i++)
                lines.get(i).add(bigChar[i]);
        }

        if (lines.isEmpty()) return new Component[0];

        Component[] preview = new Component[height];
        for (int i = 0; i < lines.size(); i++)
            preview[i] = Component.literal(String.join(characterSeparator, lines.get(i)));

        return preview;
    }
}
