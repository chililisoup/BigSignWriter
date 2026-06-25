package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigFontManager;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public final class FontInfoExtractor {
    public static Map<String, FontInfoExtraction> prepareFonts(Map<String, FontFile> fontSources) {
        return fontSources.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), new FontInfoExtraction(entry.getValue(), entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static List<FontInfo> extractAll(Map<String, FontInfoExtraction> preparedFonts) {
        preparedFonts.values().forEach(extraction -> extraction.preparedFonts = preparedFonts);
        preparedFonts.values().forEach(FontInfoExtraction::ensureInfoChecked);
        return preparedFonts.values().stream().map(FontInfoExtraction::get).toList();
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

    public static class FontInfoExtraction implements FamilyCharacterProvider {
        final FontFile fontFile;
        final String source;

        @Nullable FontInfoExtraction parentFont = null;
        @Nullable FontInfoExtraction rootAncestorFont = null;
        @Nullable Component error = null;
        @Nullable TreeSet<Character> cumulativeCharacters = null;
        String widthInfo = "0";
        @Nullable String cumulativeWidthInfo = null;

        private Map<String, FontInfoExtraction> preparedFonts = Map.of();
        private boolean relationsChecked = false;
        private boolean infoChecked = false;
        private @Nullable FontInfo result = null;

        private FontInfoExtraction(FontFile fontFile, String source) {
            this.fontFile = fontFile;
            this.source = source;
        }

        FontInfo get() {
            if (this.result == null)
                this.result = new FontInfo(this);
            return this.result;
        }

        private int height() {
            return this.fontFile.height > 0 ? this.fontFile.height : 4;
        }

        @Override
        public Map<Character, String[]> characters() {
            return this.fontFile.characters;
        }

        private void ensureRelationsChecked() {
            if (this.relationsChecked) return;
            this.relationsChecked = true;

            this.parentFont = this.findParent();
            this.rootAncestorFont = this.findRootAncestor();
        }

        @Override
        public boolean parentIsImplicit() {
            return this.fontFile.parentFont == null;
        }

        private boolean hasExplicitParent() {
            return !this.parentIsImplicit() && this.parentFont() != null;
        }

        @Override
        public @Nullable FontInfoExtraction parentFont() {
            this.ensureRelationsChecked();
            return this.parentFont;
        }

        private @Nullable FontInfoExtraction findRootAncestor() {
            if (this.parentIsImplicit()) return null;
            return this.parentFont != null && this.parentFont.hasExplicitParent() ?
                    this.parentFont.findRootAncestor() :
                    this.parentFont;
        }

        private Set<Character> cumulativeCharacters() {
            if (this.cumulativeCharacters != null) return this.cumulativeCharacters;
            if (!this.hasExplicitParent()) return this.characters().keySet();

            this.cumulativeCharacters = new TreeSet<>(FontFile::compareChars);
            FontInfoExtraction nextFont = this;
            while (nextFont != null) {
                this.cumulativeCharacters.addAll(nextFont.characters().keySet());
                nextFont = nextFont.parentFont();
            }
            return this.cumulativeCharacters;
        }

        private String widthInfo() {
            this.ensureInfoChecked();
            return this.widthInfo;
        }

        private void ensureInfoChecked() {
            if (this.infoChecked) return;
            this.infoChecked = true;
            this.error = extractInfo();
        }

        private @Nullable Component extractInfo() {
            if (this.fontFile.height <= 0) return Component.translatable(
                    "bigsignwriter.font.error.invalidHeight",
                    fontFile.height
            );

            this.parentFont = this.findParent();
            this.rootAncestorFont = this.findRootAncestor();
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

        private @Nullable FontInfoExtraction findParent() {
            if (this.source.equals(BigFontManager.DEFAULT_FONT_SOURCE)) return null;

            FontInfoExtraction parentFont = null;
            if (this.fontFile.parentFont == null) {
                if (this.height() == 4) {
                    parentFont = this.preparedFonts.get(BigFontManager.DEFAULT_FONT_SOURCE);
                } else return null;
            } else for (FontInfoExtraction extraction : this.preparedFonts.values()) {
                if (this == extraction) continue;
                if (extraction.source.equals(this.fontFile.parentFont)) {
                    parentFont = extraction;
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
    }
}
