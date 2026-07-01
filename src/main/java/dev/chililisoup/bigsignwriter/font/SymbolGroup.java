package dev.chililisoup.bigsignwriter.font;

import com.mojang.datafixers.util.Either;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public record SymbolGroup(String name, Either<Map<String, String[]>, FontInfo> symbolSource) {
    public static @Nullable SymbolGroup of(String name, @Nullable Map<String, String[]> symbols) {
        return symbols != null ? new SymbolGroup(name, Either.left(symbols)) : null;
    }

    public static @Nullable SymbolGroup of(FontInfo font) {
        return font.symbols() != null ? new SymbolGroup(font.name(), Either.right(font)) : null;
    }

    public Map<String, String[]> symbols() {
        return Optional.ofNullable(this.symbolSource.map(
                l -> l,
                FontInfo::symbols
        )).orElse(Map.of());
    }

    public @Nullable String[] get(String id) {
        return this.symbols().get(id);
    }

    public Set<Map.Entry<String, String[]>> entrySet() {
        return this.symbols().entrySet();
    }

    public static List<SymbolGroup> availableGroups() {
        ArrayList<SymbolGroup> groups = new ArrayList<>();

        List<SymbolGroup> baseGroups = baseGroups();
        groups.add(of("All", merged(baseGroups)));
        groups.addAll(baseGroups);

        return groups;
    }

    private static List<SymbolGroup> baseGroups() {
        return BigSignWriter.availableFonts().stream()
                .map(SymbolGroup::of)
                .filter(Objects::nonNull)
                .toList();
    }

    private static Map<String, String[]> merged(List<SymbolGroup> groups) {
        TreeMap<String, String[]> merged = new TreeMap<>();
        groups.forEach(group -> merged.putAll(group.expandIds()));
        return merged;
    }

    private Map<String, String[]> expandIds() {
        return this.symbolSource.map(
                l -> l,
                font -> this.entrySet().stream()
                        .map(entry -> Map.entry(
                                font.source + ":" + entry.getKey(),
                                entry.getValue()
                        )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }
}
