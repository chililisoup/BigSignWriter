package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.chililisoup.bigsignwriter.font.BuiltInFonts;
import dev.chililisoup.bigsignwriter.font.FontFile;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.font.supplier.AbstractFontSupplier;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class BigSignWriter {
    public static final String MOD_ID = "bigsignwriter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final String LOGGER_PREFIX = "[BSW] ";
    public static final Identifier ICON = id("icon.png");
    public static String VERSION;
    public static Path CONFIG_DIR;

    public static final ArrayList<FontInfo> AVAILABLE_FONTS = new ArrayList<>();
    public static @Nullable FontInfo SELECTED_FONT = null;
    public static String CHARACTER_SEPARATOR;
    public static FontInfo DEFAULT_FONT;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void initialize(String version, Path configDir) {
        VERSION = version;
        CONFIG_DIR = configDir;

        BigSignWriterConfig.init();
        reloadFonts();
    }

    public static boolean enabled() {
        return SELECTED_FONT != null;
    }

    private static Optional<String[]> getBigChar(char chr, @Nullable FontInfo fontInfo, @Nullable String[] fallback) {
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

    public static Optional<String[]> getBigChar(char chr, @Nullable FontInfo fontInfo) {
        return getBigChar(chr, fontInfo, null);
    }

    public static Optional<String[]> getBigChar(char chr) {
        return getBigChar(chr, SELECTED_FONT);
    }

    public static void selectFont(@Nullable FontInfo fontInfo) {
        SELECTED_FONT = (fontInfo != null && AVAILABLE_FONTS.contains(fontInfo)) ?
                fontInfo : null;

        CHARACTER_SEPARATOR = SELECTED_FONT != null ?
                SELECTED_FONT.characterSeparator() :
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride;
    }

    static void reselectFont(@Nullable String id) {
        if (id != null) for (FontInfo fontInfo : AVAILABLE_FONTS) {
            if (fontInfo.id.equals(id)) {
                selectFont(fontInfo);
                return;
            }
        }
        selectFont(null);
    }

    static void reselectFont() {
        selectFont(SELECTED_FONT);
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

    public static void reloadFonts() {
        String selectedFontId = SELECTED_FONT != null ? SELECTED_FONT.id : null;
        List<FontInfo> builtInFonts = copyBuiltInFonts();

        AVAILABLE_FONTS.clear();
        AVAILABLE_FONTS.addAll(builtInFonts);

        File[] jsonFiles = getFontsDir().toFile().listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles != null && jsonFiles.length > 0) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            AVAILABLE_FONTS.addAll(Arrays.stream(jsonFiles).map(file -> {
                String fileName = file.getName();
                FontInfo fontInfo = getFont(gson, file.toPath()).load().createInfo(fileName);
                if (fileName.equals("default.json")) DEFAULT_FONT = fontInfo;
                return fontInfo;
            }).toList());
        }

        AVAILABLE_FONTS.sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
        reselectFont(selectedFontId);
        LOGGER.info(LOGGER_PREFIX + "Fonts loaded!");
    }

    public static void saveFontToFile(FontInfo fontInfo) {
        String name = fontInfo.getBuiltInName();
        if (name == null) return;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            Files.createDirectories(configFonts);

            String path = name + "_copy";
            Path target = configFonts.resolve(path + ".json");
            int i = 1;
            while (Files.exists(target)) {
                target = configFonts.resolve(path + "_" + i++ + ".json");
            }

            getFont(gson, target).save(fontInfo.fontFile);
            reloadFonts();
        } catch (Exception e) {
            LOGGER.error(LOGGER_PREFIX + "Error saving font", e);
        }
    }

    private static BigSignWriterConfig.ConfigInterface<FontFile> getFont(Gson gson, Path path) {
        return new BigSignWriterConfig.ConfigInterface<>(
                gson,
                new TypeToken<>() {
                },
                path,
                new FontFile()
        );
    }

    private static List<FontInfo> copyBuiltInFonts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            Files.createDirectories(configFonts);

            return BuiltInFonts.get().entrySet().stream().map(entry -> {
                String path = entry.getKey();
                AbstractFontSupplier fontSupplier = entry.getValue();
                FontFile fontFile = fontSupplier.get();
                FontInfo fontInfo = fontFile.createInfo("builtin/" + path);

                if (path.equals("default")) DEFAULT_FONT = fontInfo;
                Path target = configFonts.resolve(path + ".json");
                if (Files.notExists(target)) return fontInfo;

                BigSignWriterConfig.ConfigInterface<FontFile> file = getFont(gson, target);
                FontFile existingFont = file.load();

                Map<Character, Set<AbstractFontSupplier.PatchCharacter>> patches = fontSupplier.patches();
                ArrayList<Character> changed = new ArrayList<>();
                ArrayList<Character> patched = new ArrayList<>();

                boolean canRemove = true;
                for (char character : fontFile.characters.keySet()) {
                    if (!existingFont.characters.containsKey(character)) {
                        existingFont.characters.put(character, fontFile.characters.get(character));
                        changed.add(character);
                        continue;
                    }

                    String existing = String.join("\n", existingFont.characters.get(character));
                    boolean match = false;

                    if (patches.containsKey(character)) {
                        for (AbstractFontSupplier.PatchCharacter patch : patches.get(character)) {
                            if (String.join("\n", patch.lines()).equals(existing)) {
                                existingFont.characters.put(character, fontFile.characters.get(character));
                                patched.add(character);
                                match = true;
                                break;
                            }
                        }
                    }

                    if (!match) match = String
                            .join("\n", fontFile.characters.get(character))
                            .equals(existing);
                    if (!match) canRemove = false;
                }

                for (char character : patches.keySet()) {
                    if (fontFile.characters.containsKey(character)) continue;
                    if (!existingFont.characters.containsKey(character)) continue;

                    String existing = String.join("\n", existingFont.characters.get(character));

                    for (AbstractFontSupplier.PatchCharacter patch : patches.get(character)) {
                        if (String.join("\n", patch.lines()).equals(existing)) {
                            existingFont.characters.remove(character);
                            patched.add(character);
                            break;
                        }
                    }

                    if (existingFont.characters.containsKey(character)) canRemove = false;
                }

                canRemove = canRemove
                        && existingFont.characters.size() == fontFile.characters.size()
                        && existingFont.characters.keySet().equals(fontFile.characters.keySet());
                if (canRemove) {
                    File targetFile = target.toFile();
                    if (targetFile.delete()) {
                        LOGGER.info(LOGGER_PREFIX + "Removed copy of built-in font '{}'", targetFile.getName());
                        return fontInfo;
                    } else LOGGER.error(LOGGER_PREFIX + "Failed to remove copy of built-in font '{}'", targetFile.getName());
                }

                boolean needsSaved = false;
                if (fontFile.credits != null && !fontFile.credits.equals(existingFont.credits)) {
                    existingFont.credits = fontFile.credits;
                    needsSaved = true;
                }
                if (!fontFile.name.equals(existingFont.name)) {
                    existingFont.name = fontFile.name;
                    needsSaved = true;
                }
                if ((fontFile.parentFont == null && existingFont.parentFont != null)
                        || (fontFile.parentFont != null && !fontFile.parentFont.equals(existingFont.parentFont))
                ) {
                    existingFont.parentFont = fontFile.parentFont;
                    needsSaved = true;
                }
                needsSaved = needsSaved || !changed.isEmpty() || !patched.isEmpty();

                if (needsSaved) {
                    file.save(existingFont);

                    if (!changed.isEmpty())
                        LOGGER.info(LOGGER_PREFIX + "Merged new characters from built-in font '{}': {}", fontFile.name, changed);
                    if (!patched.isEmpty())
                        LOGGER.info(LOGGER_PREFIX + "Patched outdated characters from built-in font '{}': {}", fontFile.name, patched);
                }

                return null;
            }).filter(Objects::nonNull).toList();
        } catch (Exception e) {
            LOGGER.error(LOGGER_PREFIX + "Error copying built-in fonts", e);
            return List.of();
        }
    }
}
