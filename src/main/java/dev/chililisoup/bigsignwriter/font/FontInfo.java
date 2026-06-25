package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigFontManager;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FontInfo implements FamilyCharacterProvider {
    public final FontFile fontFile;
    public final String source;
    private final @Nullable FontInfo parentFont;
    private final @Nullable FontInfo rootAncestorFont;
    private final @Nullable Component error;
    private final Set<Character> cumulativeCharacters;
    private final String widthInfo;
    private final @Nullable String cumulativeWidthInfo;
    private List<FontInfo> children;

    FontInfo(FontInfoExtractor.FontInfoExtraction extraction) {
        this.fontFile = extraction.fontFile;
        this.source = extraction.source;
        this.parentFont = extraction.parentFont != null ?
                extraction.parentFont.get() : null;
        this.rootAncestorFont = extraction.rootAncestorFont != null ?
                extraction.rootAncestorFont.get() : null;
        this.error = extraction.error;
        this.cumulativeCharacters = extraction.cumulativeCharacters != null ?
                extraction.cumulativeCharacters : this.characters().keySet();
        this.widthInfo = extraction.widthInfo;
        this.cumulativeWidthInfo = extraction.cumulativeWidthInfo;
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

    @Override
    public Map<Character, String[]> characters() {
        return this.fontFile.characters;
    }

    public Set<Character> cumulativeCharacters() {
        return this.cumulativeCharacters;
    }

    @Override
    public @Nullable FontInfo parentFont() {
        return this.parentFont;
    }

    public @Nullable FontInfo rootAncestorFont() {
        return this.rootAncestorFont;
    }

    @Override
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

    public boolean isDefault() {
        return this.source.equals(BigFontManager.DEFAULT_FONT_SOURCE);
    }

    public String widthInfo() {
        return this.widthInfo;
    }

    public @Nullable String cumulativeWidthInfo() {
        return this.cumulativeWidthInfo;
    }

    public @Nullable String getBuiltInName() {
        String[] fontSource = this.source.split("/");
        return fontSource.length == 2 && fontSource[0].equals("builtin") ?
                fontSource[1] : null;
    }

    public @Nullable Component error() {
        return this.error;
    }

    public List<FontInfo> children() {
        if (this.children != null) return this.children;

        this.children = BigSignWriter.availableFonts().stream()
                .filter(font -> font.rootAncestorFont() == this)
                .toList();

        return this.children;
    }

    public List<FontInfo> visibleChildren() {
        return this.children().stream().filter(FontInfo::isVisible).toList();
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
