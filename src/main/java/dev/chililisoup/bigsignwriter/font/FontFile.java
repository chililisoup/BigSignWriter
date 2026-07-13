package dev.chililisoup.bigsignwriter.font;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class FontFile {
    public static final Codec<FontFile> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("name").forGetter(FontFile::name),
            Codec.STRING.optionalFieldOf("credits").forGetter(FontFile::credits),
            Codec.INT.optionalFieldOf("height").forGetter(FontFile::height),
            Codec.STRING.optionalFieldOf("characterSeparator").forGetter(FontFile::characterSeparator),
            Codec.STRING.optionalFieldOf("parentFont").forGetter(FontFile::parentFont),
            Codec.unboundedMap(
                    Codec.INT,
                    Codec.STRING.listOf()
            ).optionalFieldOf("characters").forGetter(FontFile::characters),
            Codec.unboundedMap(
                    Codec.STRING,
                    Codec.STRING.listOf()
            ).optionalFieldOf("symbols").forGetter(FontFile::symbols)
    ).apply(i, (
            name,
            credits,
            height,
            characterSeparator,
            parentFont,
            characters,
            symbols
    ) -> {
        FontFile fontFile = new FontFile();

        fontFile.name = name;
        credits.ifPresent(fontFile::credits);
        height.ifPresent(fontFile::height);
        characterSeparator.ifPresent(fontFile::characterSeparator);
        parentFont.ifPresent(fontFile::parentFont);
        characters.ifPresent(fontFile::characters);
        symbols.ifPresent(fontFile::symbols);

        return fontFile;
    }));

    public String name = "Font";
    public @Nullable String credits = null;
    public int height = 4;
    public @Nullable String characterSeparator = null;
    public @Nullable String parentFont = null;
    public Map<Character, String[]> characters = Map.of();
    public @Nullable Map<String, String[]> symbols = null;

    public FontFile() {}

    public FontFile(String name, @NotNull String credits) {
        this.name = name;
        this.credits = credits;
    }

    private void credits(String credits) {
        this.credits = credits;
    }

    public FontFile height(int height) {
        this.height = height;
        return this;
    }

    public FontFile characterSeparator(String characterSeparator) {
        this.characterSeparator = characterSeparator;
        return this;
    }

    public FontFile parentFont(String parentFont) {
        this.parentFont = "builtin/" + parentFont;
        return this;
    }

    @SafeVarargs
    public final FontFile characters(Map.Entry<Character, String[]>... entries) {
        this.characters = new TreeMap<>(FontFile::compareChars);
        this.characters.putAll(Map.ofEntries(entries));
        return this;
    }

    private void characters(Map<Integer, List<String>> characters) {
        this.characters = characters.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> Character.toChars(entry.getKey())[0],
                        entry -> entry.getValue().toArray(String[]::new)
                ));
    }

    @SafeVarargs
    public final FontFile symbols(Map.Entry<String, String[]>... entries) {
        this.symbols = Map.ofEntries(entries);
        return this;
    }

    private void symbols(Map<String, List<String>> symbols) {
        this.symbols = symbols.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
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

    private Optional<Integer> height() {
        return Optional.of(this.height);
    }

    private Optional<String> characterSeparator() {
        return Optional.ofNullable(this.characterSeparator);
    }

    private Optional<String> parentFont() {
        return Optional.ofNullable(this.parentFont);
    }

    private Optional<Map<Integer, List<String>>> characters() {
        return Optional.of(this.characters.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        entry -> (int) entry.getKey(),
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
