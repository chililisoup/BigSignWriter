package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.resources.BigFontManager;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FontInfo implements FamilyCharacterProvider {
    public final FontFile fontFile;
    public final Identifier id;
    private final @Nullable FontInfo parentFont;
    private final @Nullable FontInfo rootAncestorFont;
    private final @Nullable Component error;
    private final Set<Character> cumulativeCharacters;
    private final String widthInfo;
    private final @Nullable String cumulativeWidthInfo;
    private final Map<String, String[]> symbols;
    private final @Nullable Component symbolError;
    private final String symbolWidthInfo;
    private final String symbolHeightInfo;
    private List<FontInfo> children;

    FontInfo(FontInfoExtractor.FontInfoExtraction extraction) {
        this.fontFile = extraction.fontFile;
        this.id = extraction.id;
        this.parentFont = extraction.parentFontInfo();
        this.rootAncestorFont = extraction.rootAncestorFont();
        this.error = extraction.error;
        this.cumulativeCharacters = extraction.cumulativeCharacters();
        this.widthInfo = extraction.widthInfo;
        this.cumulativeWidthInfo = extraction.cumulativeWidthInfo;
        this.symbols = extraction.symbols();
        this.symbolError = extraction.symbolError;
        this.symbolWidthInfo = extraction.symbolWidthInfo;
        this.symbolHeightInfo = extraction.symbolHeightInfo;
    }

    public String name() {
        return this.fontFile.name;
    }

    public @Nullable String credits() {
        return this.fontFile.credits;
    }

    public int height() {
        return this.fontFile.getHeight();
    }

    public String characterSeparator() {
        return BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverrideEnabled ?
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride :
                (this.fontFile.characterSeparator != null ? this.fontFile.characterSeparator : " ");
    }

    @Override
    public Map<Character, String[]> characters() {
        return this.fontFile.getCharacters();
    }

    public Set<Character> cumulativeCharacters() {
        return this.cumulativeCharacters;
    }

    public Map<String, String[]> symbols() {
        return this.symbols;
    }

    public Map<String, String[]> explicitSymbols() {
        return this.fontFile.symbols != null ? this.fontFile.symbols : Map.of();
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
        return this.fontFile.parentFont().isEmpty();
    }

    public boolean hasExplicitParent() {
        return !this.parentIsImplicit() && this.parentFont() != null;
    }

    public boolean hasCharacters() {
        return !this.cumulativeCharacters.isEmpty();
    }

    public boolean hasSymbols() {
        return !this.symbols.isEmpty();
    }

    public boolean hasExplicitSymbols() {
        return !this.explicitSymbols().isEmpty();
    }

    public boolean isBroken() {
        return this.error() != null;
    }

    public boolean isWorking() {
        return !this.isBroken();
    }

    public boolean areSymbolsBroken() {
        return this.symbolError() != null;
    }

    public boolean areSymbolsWorking() {
        return !this.areSymbolsBroken();
    }

    public boolean isFromConfigFolder() {
        return this.id.getNamespace().equals(BigSignWriter.MOD_ID)
                && this.id.getPath().startsWith("user/")
                && this.id.getPath().endsWith(".json");
    }

    public boolean isVisible(BigSignWriterConfig.PersistentConfig config) {
        return !config.isFontHidden(this.id);
    }

    public boolean isVisible() {
        return this.isVisible(BigSignWriterConfig.MAIN_CONFIG);
    }

    public void setVisible(BigSignWriterConfig.PersistentConfig config, boolean visible) {
        if (visible) config.showFont(this.id);
        else config.hideFont(this.id);
    }

    public boolean isDefault() {
        return this.id.equals(BigFontManager.DEFAULT_FONT_ID);
    }

    public @Nullable Component error() {
        return this.error;
    }

    public String widthInfo() {
        return this.widthInfo;
    }

    public @Nullable String cumulativeWidthInfo() {
        return this.cumulativeWidthInfo;
    }

    public @Nullable Component symbolError() {
        return this.symbolError;
    }

    public String symbolWidthInfo() {
        return this.symbolWidthInfo;
    }

    public String symbolHeightInfo() {
        return this.symbolHeightInfo;
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
        return this.hasCharacters() ?
                this.getPreview(this.name()) :
                new Component[]{ Component.literal(this.name()) };
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

    public final List<Component[]> getWrappedFontPreview(String text, int width) {
        return getWrappedFontPreview(
                this,
                text,
                this.characterSeparator().isEmpty() ? " " : this.characterSeparator(),
                width
        );
    }

    private static List<Component[]> getWrappedFontPreview(FontInfo fontInfo, String text, String characterSeparator, int width) {
        Font font = Minecraft.getInstance().font;
        float separatorWidth = font.width(characterSeparator);

        ArrayList<Component[]> previewLines = new ArrayList<>();
        StringBuilder runningString = new StringBuilder();
        float runningWidth = 0F;

        for (char chr : text.toCharArray()) {
            String top = BigSignWriter.getBigChar(chr, fontInfo).orElse(new String[]{""})[0];

            float chrWidth = font.width(top);
            if (runningWidth > 0 && runningWidth + chrWidth > width) {
                if (!runningString.isEmpty())
                    previewLines.add(fontInfo.getPreview(runningString.toString(), characterSeparator));

                runningWidth = separatorWidth + chrWidth;
                runningString = new StringBuilder(String.valueOf(chr));
            } else {
                runningWidth += separatorWidth + chrWidth;
                runningString.append(chr);
            }
        }

        if (!runningString.isEmpty())
            previewLines.add(fontInfo.getPreview(runningString.toString(), characterSeparator));

        return previewLines;
    }

    public final List<Component[]> getWrappedSymbolsPreview(int width) {
        String characterSeparator = this.characterSeparator().isEmpty() ? " " : this.characterSeparator();
        return getWrappedSymbolsPreview(
                this.symbols.values(),
                characterSeparator + characterSeparator,
                width
        );
    }

    private static List<Component[]> getWrappedSymbolsPreview(Collection<String[]> symbols, String characterSeparator, int width) {
        Font font = Minecraft.getInstance().font;
        float separatorWidth = font.width(characterSeparator);

        ArrayList<Component[]> previewLines = new ArrayList<>();
        ArrayList<String[]> runningLine = new ArrayList<>();
        float runningWidth = 0F;

        for (String[] symbol : symbols) {
            float symbolWidth = font.width(symbol[0]);
            if (runningWidth > 0 && runningWidth + symbolWidth > width) {
                if (!runningLine.isEmpty())
                    previewLines.add(joinSymbols(runningLine, characterSeparator));

                runningWidth = separatorWidth + symbolWidth;
                runningLine.clear();
                runningLine.add(symbol);
            } else {
                runningWidth += separatorWidth + symbolWidth;
                runningLine.add(symbol);
            }
        }

        if (!runningLine.isEmpty())
            previewLines.add(joinSymbols(runningLine, characterSeparator));

        return previewLines;
    }

    private static Component[] joinSymbols(ArrayList<String[]> separatedSymbols, String characterSeparator) {
        int height = separatedSymbols.stream()
                .map(symbol -> symbol.length)
                .max(Integer::compareTo)
                .orElse(0);
        if (height == 0) return new Component[0];

        ArrayList<ArrayList<String>> lines = new ArrayList<>(height);
        for (int i = 0; i < height; i++) lines.add(new ArrayList<>());

        Font font = Minecraft.getInstance().font;
        for (String[] symbol : separatedSymbols) {
            String filler = symbol.length < height ?
                    ModUtil.getGapFiller(font.width(symbol[0])) :
                    "";
            for (int i = 0; i < height; i++)
                lines.get(i).add(i < symbol.length ? symbol[i] : filler);
        }

        Component[] preview = new Component[height];
        for (int i = 0; i < lines.size(); i++)
            preview[i] = Component.literal(String.join(characterSeparator, lines.get(i)));

        return preview;
    }
}
