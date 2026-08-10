package dev.chililisoup.bigsignwriter.resources;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.BigSignWriterConfig;
import dev.chililisoup.bigsignwriter.font.FontFile;

import java.nio.file.Path;
import java.util.TreeMap;

public final class UserSymbolsManager {
    private static final TreeMap<String, String[]> USER_SYMBOLS = new TreeMap<>();

    public static String filterKey(String key) {
        String filtered = key.toLowerCase().replace(" ", "_").replaceAll("[^a-z0-9_./]", "");
        return filtered.substring(0, Math.min(32, filtered.length()));
    }

    public static boolean put(String key, String[] symbol) {
        if (symbol.length == 0) return false;
        String filtered = filterKey(key);
        if (filtered.isEmpty()) return false;
        USER_SYMBOLS.put(key, symbol);
        updateFile();
        return true;
    }

    public static boolean containsKey(String key) {
        return USER_SYMBOLS.containsKey(key);
    }

    public static void reload() {
        populateUserSymbols(getUserSymbolsFile());
    }

    private static FontFile createEmpty() {
        FontFile empty = new FontFile();
        empty.name = "User Symbols";
        return empty;
    }

    private static void updateFile() {
        FontFile fontFile = getUserSymbolsFile();
        fontFile.symbols = USER_SYMBOLS;
        getUserFontInterface(userSymbolsPath()).save(fontFile);
        BigSignWriter.reloadUserFonts();
    }

    private static Path userSymbolsPath() {
        return BigSignWriter.getFontsDir().resolve("user_symbols.json");
    }

    private static FontFile getUserSymbolsFile() {
        Path target = userSymbolsPath();
        return target.toFile().exists() ?
                getUserFontInterface(target).load() :
                createEmpty();
    }

    private static void populateUserSymbols(FontFile source) {
        USER_SYMBOLS.clear();
        if (source.symbols != null) USER_SYMBOLS.putAll(source.symbols);
    }

    private static BigSignWriterConfig.ConfigInterface<FontFile> getUserFontInterface(Path path) {
        return new BigSignWriterConfig.ConfigInterface<>(
                new GsonBuilder().setPrettyPrinting().create(), new TypeToken<>() {}, path, createEmpty()
        );
    }
}
