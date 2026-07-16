package dev.chililisoup.bigsignwriter.util;

import java.util.LinkedHashMap;
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
}
