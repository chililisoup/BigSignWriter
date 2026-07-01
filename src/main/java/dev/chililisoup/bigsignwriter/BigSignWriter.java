package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.chililisoup.bigsignwriter.font.FamilyCharacterProvider;
import dev.chililisoup.bigsignwriter.font.FontFile;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.font.SymbolGroup;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class BigSignWriter {
    private static final BigFontManager BIG_FONT_MANAGER = new BigFontManager();
    public static final String MOD_ID = "bigsignwriter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final String LOGGER_PREFIX = "[BSW] ";
    public static final Identifier ICON = id("icon.png");
    public static String VERSION;
    public static Path CONFIG_DIR;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void initialize(String version, Path configDir) {
        VERSION = version;
        CONFIG_DIR = configDir;

        BigSignWriterConfig.init();
    }

    public static boolean enabled() {
        return selectedFont() != null;
    }

    public static void forceReload() {
        BIG_FONT_MANAGER.forceReload();
    }

    public static List<FontInfo> availableFonts() {
        return BIG_FONT_MANAGER.availableFonts();
    }

    public static List<SymbolGroup> availableSymbolGroups() {
        return BIG_FONT_MANAGER.availableSymbolGroups();
    }

    public static @Nullable FontInfo selectedFont() {
        return BIG_FONT_MANAGER.selectedFont();
    }

    public static String characterSeparator() {
        return BIG_FONT_MANAGER.characterSeparator();
    }

    public static int height() {
        FontInfo selected = selectedFont();
        return selected != null ? selected.height() : 1;
    }

    public static BigFontManager getBigFontManager() {
        return BIG_FONT_MANAGER;
    }

    public static void selectFont(@Nullable FontInfo fontInfo) {
        BIG_FONT_MANAGER.selectFont(fontInfo);
    }

    static void reselectFont() {
        BIG_FONT_MANAGER.reselectFont();
    }

    private static Optional<String[]> getBigChar(char chr, @Nullable FamilyCharacterProvider fontInfo, @Nullable String[] fallback) {
        if (fontInfo == null)
            return Optional.ofNullable(fallback);

        if (fontInfo.characters() == null)
            return Optional.ofNullable(fallback);

        if (fontInfo.characters().containsKey(chr))
            return Optional.of(fontInfo.characters().get(chr));

        if (fallback == null) {
            char upper = Character.toUpperCase(chr);
            if (fontInfo.characters().containsKey(upper)) {
                fallback = fontInfo.characters().get(upper);
                return fontInfo.parentIsImplicit() ?
                        Optional.of(fallback) :
                        getBigChar(chr, fontInfo.parentFont(), fallback);
            }
        }

        return getBigChar(chr, fontInfo.parentFont(), fallback);
    }

    public static Optional<String[]> getBigChar(char chr, @Nullable FamilyCharacterProvider fontInfo) {
        return getBigChar(chr, fontInfo, null);
    }

    public static Optional<String[]> getBigChar(char chr) {
        return getBigChar(chr, selectedFont());
    }

    public static @NotNull Path getFontsDir() {
        Path fontsDir = BigSignWriterConfig.getConfigDir().resolve("fonts");
        try {
            Files.createDirectories(fontsDir);
        } catch (IOException e) {
            LOGGER.error(LOGGER_PREFIX + "Failed to create fonts directory: {}", fontsDir, e);
        }
        return fontsDir;
    }

    public static void copyFontToFile(FontInfo fontInfo) {
        String name = fontInfo.getBuiltInName();
        if (name == null) return;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            String path = name + "_copy";
            String fontName = fontInfo.name() + " Copy";
            Path target = configFonts.resolve(path + ".json");
            int i = 1;
            while (Files.exists(target)) {
                fontName = fontInfo.name() + " Copy (" + i + ")";
                target = configFonts.resolve(path + "_" + i++ + ".json");
            }

            FontFile copy = fontInfo.fontFile.copyWithUnsafeCharacters();
            copy.name = fontName;
            getFontFileInterface(gson, target).save(copy);
            forceReload();
        } catch (Exception e) {
            LOGGER.error(LOGGER_PREFIX + "Error saving font", e);
        }
    }

    static BigSignWriterConfig.ConfigInterface<FontFile> getFontFileInterface(Gson gson, Path path) {
        return new BigSignWriterConfig.ConfigInterface<>(
                gson,
                new TypeToken<>() {
                },
                path,
                new FontFile()
        );
    }
}
