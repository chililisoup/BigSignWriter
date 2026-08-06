package dev.chililisoup.bigsignwriter.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class ModUtil {
    public static <T, K, U> Collector<T, ?, LinkedHashMap<K, U>> orderedMapCollector(
            Function<T, K> keyMapper, Function<T, U> valueMapper
    ) {
        return Collectors.toMap(
                keyMapper,
                valueMapper,
                (x, y) -> y,
                LinkedHashMap::new
        );
    }

    public static <K, U> Collector<Map.Entry<K, U>, ?, LinkedHashMap<K, U>> orderedMapCollector() {
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

    public static Either<String[], Component> validateSymbolForSave(String[] lines) {
        int startLine = -1;
        int endLine = -1;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) continue;
            if (startLine == -1) startLine = i;
            endLine = i;
        }

        if (startLine == -1) return Either.right(Component.translatable("bigsignwriter.symbols.save.error.empty"));

        Font font = Minecraft.getInstance().font;
        String[] symbol = Arrays.stream(lines, startLine, endLine + 1).toArray(String[]::new);

        int[] widths = new int[symbol.length];
        int topWidth = font.width(symbol[0]);
        widths[0] = topWidth;

        boolean unfixed = false;
        for (int i = 1; i < symbol.length; i++) {
            widths[i] = font.width(symbol[i]);
            if (widths[i] != widths[0]) unfixed = true;
        }
        if (unfixed) return Either.right(Component.translatable(
                "bigsignwriter.symbols.save.error.unfixedWidth",
                Arrays.toString(widths)
        ));

        return Either.left(symbol);
    }
}
