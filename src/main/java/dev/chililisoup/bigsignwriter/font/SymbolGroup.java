package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.util.ModUtil;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public record SymbolGroup(
        Identifier id,
        String name,
        Map<String, SymbolReference> symbols,
        VisibilityChecker visibilityChecker,
        boolean isMerged
) {
    public static @Nullable SymbolGroup of(
            Identifier id,
            String name,
            Map<String, SymbolReference> symbols,
            VisibilityChecker visibilityChecker,
            boolean isMerged
    ) {
        return !symbols.isEmpty() ? new SymbolGroup(id, name, symbols, visibilityChecker, isMerged) : null;
    }

    public static @Nullable SymbolGroup of(FontInfo font) {
        return of(
                font.id,
                font.name(),
                font.symbols().entrySet().stream().collect(ModUtil.orderedMapCollector(
                        Map.Entry::getKey,
                        entry -> new SymbolReference(entry.getKey(), font)
                )),
                font::isVisible,
                false
        );
    }

    public static @Nullable SymbolGroup ofMerged(Identifier id, String name, List<SymbolGroup> groups) {
        return of(id, name, merged(groups), config -> true, true);
    }

    public Set<Map.Entry<String, SymbolReference>> entrySet() {
        return this.symbols().entrySet();
    }

    public boolean isVisible(BigSignWriterConfig.PersistentConfig config) {
        return this.visibilityChecker.check(config);
    }

    public boolean isVisible() {
        return this.visibilityChecker.check();
    }

    public static List<SymbolGroup> availableGroups() {
        return BigSignWriter.availableFonts().stream()
                .map(SymbolGroup::of)
                .filter(Objects::nonNull)
                .toList();
    }

    private static Map<String, SymbolReference> merged(List<SymbolGroup> groups) {
        LinkedHashMap<String, SymbolReference> merged = new LinkedHashMap<>();
        groups.stream()
                .sorted(Comparator.comparing(SymbolGroup::name))
                .forEach(group -> merged.putAll(
                        group.expandIds(SymbolGroup::filterFromInclude)
                ));

        return merged;
    }

    public static boolean filterFromInclude(String key) {
        if (!BigSignWriterConfig.MAIN_CONFIG.nonUSCharactersInSymbols) return true;
        char[] chars = key.toCharArray();
        return chars.length != 1 || String.valueOf(chars[0]).matches("[ -~]");
    }

    private Map<String, SymbolReference> expandIds(Function<String, Boolean> filter) {
        return this.symbols.entrySet().stream()
                .filter(entry -> filter.apply(entry.getKey()))
                .collect(ModUtil.orderedMapCollector(
                        entry -> entry.getValue().expandedId(),
                        Map.Entry::getValue
                ));
    }

    @FunctionalInterface
    public interface VisibilityChecker {
        boolean check(BigSignWriterConfig.PersistentConfig config);

        default boolean check() {
            return this.check(BigSignWriterConfig.MAIN_CONFIG);
        }
    }
}
