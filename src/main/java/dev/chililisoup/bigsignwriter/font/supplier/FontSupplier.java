package dev.chililisoup.bigsignwriter.font.supplier;

import dev.chililisoup.bigsignwriter.font.FontFile;

import java.util.HashMap;
import java.util.Map;

public interface FontSupplier {
    FontFile get();

    default Map<Character, String[][]> patches() {
        return new HashMap<>();
    }
}
