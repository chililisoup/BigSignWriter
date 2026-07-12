package dev.chililisoup.bigsignwriter.font;

import com.mojang.datafixers.util.Either;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record SymbolGroup(String name, Either<Map<String, String[]>, FontInfo> symbolSource) {
    public static @Nullable SymbolGroup of(String name, @Nullable Map<String, String[]> symbols) {
        return (symbols != null && !symbols.isEmpty()) ?
                new SymbolGroup(name, Either.left(symbols)) : null;
    }

    public static @Nullable SymbolGroup of(FontInfo font) {
        return font.hasSymbols() ? new SymbolGroup(font.name(), Either.right(font)) : null;
    }

    public static @Nullable SymbolGroup ofMerged(String name, List<SymbolGroup> groups) {
        return of(name, merged(groups));
    }

    public Map<String, String[]> symbols() {
        return this.symbolSource.map(
                l -> l,
                FontInfo::symbols
        );
    }

    public @Nullable String[] get(String id) {
        return this.symbols().get(id);
    }

    public Set<Map.Entry<String, String[]>> entrySet() {
        return this.symbols().entrySet();
    }

    public boolean isVisible(BigSignWriterConfig.PersistentConfig config) {
        return this.symbolSource.map(
                l -> true,
                font -> font.isVisible(config)
        );
    }

    public boolean isVisible() {
        return this.isVisible(BigSignWriterConfig.MAIN_CONFIG);
    }

    public static List<SymbolGroup> availableGroups() {
        return BigSignWriter.availableFonts().stream()
                .map(SymbolGroup::of)
                .filter(Objects::nonNull)
                .toList();
    }

    private static Map<String, String[]> merged(List<SymbolGroup> groups) {
        TreeMap<String, String[]> merged = new TreeMap<>();
        groups.forEach(group -> merged.putAll(group.expandIds(SymbolGroup::filterFromInclude)));
        return merged;
    }

    private static boolean filterFromInclude(String key) {
        char[] chars = key.toCharArray();
        if (chars.length != 1) return true;
        return !BigSignWriterConfig.MAIN_CONFIG.characterShownInSymbols(chars[0]);
    }

    private Map<String, String[]> expandIds(Function<String, Boolean> filter) {
        return this.symbolSource.map(
                l -> l,
                font -> this.entrySet().stream()
                        .filter(entry -> filter.apply(entry.getKey()))
                        .map(entry -> Map.entry(
                                font.source + ":" + entry.getKey(),
                                entry.getValue()
                        )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
