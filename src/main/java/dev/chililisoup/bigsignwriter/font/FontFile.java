package dev.chililisoup.bigsignwriter.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.chililisoup.bigsignwriter.util.ModUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class FontFile {
    public static final Codec<FontFile> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("name").forGetter(FontFile::name),
            Codec.STRING.optionalFieldOf("credits").forGetter(FontFile::credits),
            Codec.STRING.listOf().optionalFieldOf("license").forGetter(FontFile::license),
            Codec.INT.optionalFieldOf("height").forGetter(FontFile::height),
            Codec.STRING.optionalFieldOf("characterSeparator").forGetter(FontFile::characterSeparator),
            Identifier.CODEC.optionalFieldOf("parentFont").forGetter(FontFile::parentFont),
            Codec.unboundedMap(
                    ExtraCodecs.CODEPOINT,
                    Codec.STRING.listOf()
            ).optionalFieldOf("characters").forGetter(FontFile::characters),
            Codec.unboundedMap(
                    Codec.STRING,
                    Codec.STRING.listOf()
            ).optionalFieldOf("symbols").forGetter(FontFile::symbols)
    ).apply(i, (
            name,
            credits,
            license,
            height,
            characterSeparator,
            parentFont,
            characters,
            symbols
    ) -> {
        FontFile fontFile = new FontFile();

        fontFile.name = name;
        credits.ifPresent(fontFile::credits);
        license.ifPresent(fontFile::license);
        height.ifPresent(fontFile::height);
        characterSeparator.ifPresent(fontFile::characterSeparator);
        parentFont.ifPresent(fontFile::parentFont);
        characters.ifPresent(fontFile::characters);
        symbols.ifPresent(fontFile::symbols);

        return fontFile;
    }));

    public String name = "Font";
    public @Nullable String credits = null;
    public @Nullable String[] license = null;
    public @Nullable Integer height = null;
    public @Nullable String characterSeparator = null;
    private @Nullable String parentFont = null;
    public @Nullable Map<Character, String[]> characters = null;
    public @Nullable Map<String, String[]> symbols = null;

    public int getHeight() {
        return this.height != null && this.height > 0 ? this.height : 4;
    }

    public Map<Character, String[]> getCharacters() {
        return this.characters != null ? this.characters : Map.of();
    }

    private void credits(String credits) {
        this.credits = credits;
    }

    private void license(List<String> license) {
        this.license = license.toArray(String[]::new);
    }

    public void height(int height) {
        this.height = height;
    }

    public void characterSeparator(String characterSeparator) {
        this.characterSeparator = characterSeparator;
    }

    public void parentFont(Identifier parentFont) {
        this.parentFont = parentFont.toString();
    }

    private void characters(Map<Integer, List<String>> characters) {
        this.characters = characters.entrySet().stream()
                .collect(ModUtil.orderedMapCollector(
                        entry -> Character.toChars(entry.getKey())[0],
                        entry -> entry.getValue().toArray(String[]::new)
                ));
    }

    private void symbols(Map<String, List<String>> symbols) {
        this.symbols = symbols.entrySet().stream()
                .collect(ModUtil.orderedMapCollector(
                        Map.Entry::getKey,
                        entry -> entry.getValue().toArray(String[]::new)
                ));
    }

    public FontFile copyWithUnsafeCharacters() {
        FontFile copy = new FontFile();

        copy.name = this.name;
        copy.credits = this.credits;
        copy.height = height;
        copy.characterSeparator = this.characterSeparator;
        copy.parentFont = this.parentFont;
        copy.characters = this.characters;
        copy.symbols = this.symbols;

        return copy;
    }

    private String name() {
        return this.name;
    }

    private Optional<String> credits() {
        return Optional.ofNullable(this.credits);
    }

    private Optional<List<String>> license() {
        return Optional.ofNullable(this.license).map(List::of);
    }

    private Optional<Integer> height() {
        return Optional.ofNullable(this.height);
    }

    private Optional<String> characterSeparator() {
        return Optional.ofNullable(this.characterSeparator);
    }

    public Optional<Identifier> parentFont() {
        return Optional.ofNullable(this.parentFont).map(Identifier::tryParse);
    }

    private Optional<Map<Integer, List<String>>> characters() {
        return Optional.ofNullable(this.characters)
                .map(characters -> characters.entrySet().stream()
                        .collect(Collectors.toUnmodifiableMap(
                                entry -> Character.getNumericValue(entry.getKey()),
                                entry -> List.of(entry.getValue())
                        ))
                );
    }

    private Optional<Map<String, List<String>>> symbols() {
        return Optional.ofNullable(this.symbols).map(symbols -> symbols.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.of(entry.getValue())
                ))
        );
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
