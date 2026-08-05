package dev.chililisoup.bigsignwriter.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class ModUtil {
    public static <T, K, U> Collector<T, ?, SequencedMap<K, U>> orderedMapCollector(
            Function<T, K> keyMapper, Function<T, U> valueMapper
    ) {
        return Collectors.toMap(
                keyMapper,
                valueMapper,
                (x, y) -> y,
                LinkedHashMap::new
        );
    }

    public static <K, U> Collector<Map.Entry<K, U>, ?, SequencedMap<K, U>> orderedMapCollector() {
        return orderedMapCollector(Map.Entry::getKey, Map.Entry::getValue);
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static String getGapFiller(int width) {
        return width <= 0 ? "" : switch (width) {
            case 1 -> "\u073C";
            case 2 -> "\u073C\u073C";
            case 3 -> "\u073C\u073C\u073C";
            case 6 -> " \u073C";
            case 7 -> " \u073C\u073C";
            case 11 -> "  \u073C";
            default -> {
                String filler = "";
                while (width % 5 != 0) {
                    filler += " ";
                    width -= 4;
                }
                while (width > 0) {
                    filler += " ";
                    width -= 5;
                }
                yield filler;
            }
        };
    }
}
