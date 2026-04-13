package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class FontInfo {
    public final FontFile fontFile;
    public final String source;
    public final String id;
    private @Nullable FontInfo parentFont = null;
    private boolean parentChecked = false;
    private boolean infoChecked = false;
    private @Nullable Component error = null;
    private int minWidth;
    private int maxWidth;
    private float avgWidth;

    FontInfo(FontFile fontFile, String source) {
        this.fontFile = fontFile;
        this.source = source;

        String builtInName = this.getBuiltInName();
        if (builtInName != null) this.id = builtInName;
        else {
            int index = source.lastIndexOf(".json");
            this.id = index > 0 ? source.substring(0, index) : source;
        }
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

    public @Nullable FontInfo parentFont() {
        if (this.parentChecked) return this.parentFont;
        this.parentChecked = true;
        if (this == BigSignWriter.DEFAULT_FONT) return null;

        if (this.fontFile.parentFont == null) {
            if (this.height() == 4) {
                this.parentFont = BigSignWriter.DEFAULT_FONT;
            } else return null;
        } else for (FontInfo fontInfo : BigSignWriter.AVAILABLE_FONTS) {
            if (this == fontInfo) continue;
            if (fontInfo.id.equals(this.fontFile.parentFont)) {
                this.parentFont = fontInfo;
                break;
            }
        }

        if (this.parentFont == null) return null;

        for (char chr : this.parentFont.characters().keySet()) {
            if (!this.characters().containsKey(chr)
                    && !this.characters().containsKey(Character.toUpperCase(chr))
            ) return this.parentFont;
        }

        this.parentFont = null;
        return null;
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

    public String widthInfo() {
        if (this.isBroken()) return "UNFIXED";
        return this.minWidth == this.maxWidth ?
                String.valueOf(this.minWidth) :
                String.format(
                        "%d-%d ~%.2f",
                        this.minWidth,
                        this.maxWidth,
                        this.avgWidth
                );
    }

    public @Nullable String getBuiltInName() {
        String[] fontSource = this.source.split("/");
        return fontSource.length == 2 && fontSource[0].equals("builtin") ?
                fontSource[1] : null;
    }

    public @Nullable Component error() {
        if (this.infoChecked) return this.error;
        this.error = this.extractInfo(fontFile);
        this.infoChecked = true;
        return this.error;
    }

    private @Nullable Component extractInfo(FontFile fontFile) {
        if (fontFile.height <= 0) return Component.translatable(
                "bigsignwriter.font.error.invalidHeight",
                fontFile.height
        );
        if (fontFile.characters.isEmpty()) return null;

        Font font = Minecraft.getInstance().font;
        ArrayList<Integer> allWidths = new ArrayList<>(fontFile.characters.size());

        for (Map.Entry<Character, String[]> entry : fontFile.characters.entrySet()) {
            char baseChar = entry.getKey();
            String[] bigChar = entry.getValue();

            if (bigChar.length != fontFile.height) return Component.translatable(
                    "bigsignwriter.font.error.wrongLineCount",
                    String.valueOf(baseChar),
                    bigChar.length,
                    fontFile.height
            );

            int[] widths = new int[bigChar.length];
            int topWidth = font.width(bigChar[0]);
            widths[0] = topWidth;
            allWidths.add(topWidth);
            boolean unfixed = false;
            for (int i = 1; i < bigChar.length; i++) {
                widths[i] = font.width(bigChar[i]);
                if (widths[i] != widths[0]) unfixed = true;
            }
            if (unfixed) return Component.translatable(
                    "bigsignwriter.font.error.unfixedWidth",
                    String.valueOf(baseChar),
                    Arrays.toString(widths)
            );
        }

        this.minWidth = Collections.min(allWidths);
        this.maxWidth = Collections.max(allWidths);
        this.avgWidth = (float) allWidths.stream().mapToInt(Integer::intValue).sum() / allWidths.size();

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
