package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FontInfo {
    public final FontFile fontFile;
    public final String source;
    public final String id;
    private boolean infoChecked = false;
    private @Nullable Component error = null;
    private @Nullable FontInfo parentFont = null;
    private @Nullable TreeSet<Character> cumulativeCharacters = null;
    private String widthInfo = "0";
    private @Nullable String cumulativeWidthInfo = null;

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

    public Set<Character> cumulativeCharacters() {
        if (this.cumulativeCharacters != null) return this.cumulativeCharacters;
        if (!this.hasExplicitParent()) return this.characters().keySet();

        this.cumulativeCharacters = new TreeSet<>(FontFile.COMPARATOR);
        FontInfo nextFont = this;
        while (nextFont != null) {
            this.cumulativeCharacters.addAll(nextFont.characters().keySet());
            nextFont = nextFont.parentFont();
        }
        return this.cumulativeCharacters;
    }

    public @Nullable FontInfo parentFont() {
        this.ensureInfoReady();
        return this.parentFont;
    }

    public boolean parentIsImplicit() {
        return this.fontFile.parentFont == null;
    }

    public boolean hasExplicitParent() {
        return !this.parentIsImplicit() && this.parentFont() != null;
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
        this.ensureInfoReady();
        return this.widthInfo;
    }

    public @Nullable String cumulativeWidthInfo() {
        this.ensureInfoReady();
        return this.cumulativeWidthInfo;
    }

    public @Nullable String getBuiltInName() {
        String[] fontSource = this.source.split("/");
        return fontSource.length == 2 && fontSource[0].equals("builtin") ?
                fontSource[1] : null;
    }

    public @Nullable Component error() {
        this.ensureInfoReady();
        return this.error;
    }

    private void ensureInfoReady() {
        if (!this.infoChecked) {
            this.infoChecked = true;
            this.error = this.extractInfo();
        }
    }

    private @Nullable Component extractInfo() {
        if (this.fontFile.height <= 0) return Component.translatable(
                "bigsignwriter.font.error.invalidHeight",
                fontFile.height
        );

        this.parentFont = this.findParent();
        if (this.fontFile.characters.isEmpty()) {
            if (!this.parentIsImplicit() && this.parentFont != null)
                this.cumulativeWidthInfo = this.parentFont.widthInfo();
            return null;
        }

        Font font = Minecraft.getInstance().font;
        Set<Character> cumulativeCharacters = this.cumulativeCharacters();
        ArrayList<Integer> ownWidths = new ArrayList<>(this.fontFile.characters.size());
        ArrayList<Integer> cumulativeWidths = new ArrayList<>(cumulativeCharacters.size());

        for (char chr : cumulativeCharacters) {
            String[] bigChar = this.fontFile.characters.get(chr);
            if (bigChar == null && this.parentFont != null)
                bigChar = BigSignWriter.getBigChar(chr, this.parentFont).orElse(null);
            if (bigChar == null) continue;

            if (bigChar.length != this.fontFile.height) return Component.translatable(
                    "bigsignwriter.font.error.wrongLineCount",
                    String.valueOf(chr),
                    bigChar.length,
                    this.fontFile.height
            );

            int[] widths = new int[bigChar.length];
            int topWidth = font.width(bigChar[0]);
            widths[0] = topWidth;

            if (this.fontFile.characters.containsKey(chr)) {
                boolean unfixed = false;
                for (int i = 1; i < bigChar.length; i++) {
                    widths[i] = font.width(bigChar[i]);
                    if (widths[i] != widths[0]) unfixed = true;
                }
                if (unfixed) return Component.translatable(
                        "bigsignwriter.font.error.unfixedWidth",
                        String.valueOf(chr),
                        Arrays.toString(widths)
                );

                ownWidths.add(topWidth);
            }

            cumulativeWidths.add(topWidth);
        }

        this.widthInfo = createWidthInfo(ownWidths);
        if (!this.parentIsImplicit()) {
            String cumulativeWidthInfo = createWidthInfo(cumulativeWidths);
            if (!this.widthInfo.equals(cumulativeWidthInfo))
                this.cumulativeWidthInfo = cumulativeWidthInfo;
        }

        return null;
    }

    private static String createWidthInfo(ArrayList<Integer> widths) {
        int minWidth = Collections.min(widths);
        int maxWidth = Collections.max(widths);
        return minWidth == maxWidth ?
                String.valueOf(minWidth) :
                String.format(
                        "%d-%d ~%.2f",
                        minWidth,
                        maxWidth,
                        (float) widths.stream().mapToInt(Integer::intValue).sum() / widths.size()
                );
    }

    private @Nullable FontInfo findParent() {
        if (this == BigSignWriter.DEFAULT_FONT) return null;

        FontInfo parentFont = null;
        if (this.fontFile.parentFont == null) {
            if (this.height() == 4) {
                parentFont = BigSignWriter.DEFAULT_FONT;
            } else return null;
        } else for (FontInfo fontInfo : BigSignWriter.AVAILABLE_FONTS) {
            if (this == fontInfo) continue;
            if (fontInfo.id.equals(this.fontFile.parentFont)) {
                parentFont = fontInfo;
                break;
            }
        }

        if (parentFont == null) return null;
        if (parentFont.height() != this.height()) return null;

        boolean explicit = !this.parentIsImplicit();
        for (char chr : parentFont.characters().keySet()) {
            if (!this.characters().containsKey(chr)
                    && (explicit || !this.characters().containsKey(Character.toUpperCase(chr)))
            ) return parentFont;
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
