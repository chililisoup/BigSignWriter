package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.chililisoup.bigsignwriter.font.BuiltInFonts;
import dev.chililisoup.bigsignwriter.font.FontFile;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.font.supplier.FontSupplier;
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

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else {
/*import net.neoforged.fml.ModList;
*///?}

public class BigSignWriter {
    public static final String MOD_ID = "bigsignwriter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Identifier ICON = id("icon.png");
    public static final String VERSION;

    public static final ArrayList<FontInfo> AVAILABLE_FONTS = new ArrayList<>();
    public static @Nullable FontInfo SELECTED_FONT = null;
    public static String CHARACTER_SEPARATOR;
    public static FontInfo DEFAULT_FONT;

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void initialize() {
        BigSignWriterConfig.init();
        reloadFonts();
    }

    static {
        VERSION =
                //? if fabric {
                FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
                //?} else {
                /*ModList.get().getModFileById(MOD_ID).versionString();
                *///?}
    }

    public static boolean enabled() {
        return SELECTED_FONT != null;
    }

    public static Optional<String[]> getBigChar(char chr, @Nullable FontInfo fontInfo) {
        if (fontInfo == null)
            return Optional.empty();

        if (fontInfo.characters() == null)
            return Optional.empty();

        if (fontInfo.characters().containsKey(chr))
            return Optional.of(fontInfo.characters().get(chr));

        char upper = Character.toUpperCase(chr);
        if (fontInfo.characters().containsKey(upper))
            return Optional.of(fontInfo.characters().get(upper));

        if (DEFAULT_FONT == null || fontInfo.name().equals("Default") || fontInfo.height() != 4)
            return Optional.empty();

        if (DEFAULT_FONT.characters().containsKey(chr))
            return Optional.of(DEFAULT_FONT.characters().get(chr));

        if (DEFAULT_FONT.characters().containsKey(upper))
            return Optional.of(DEFAULT_FONT.characters().get(upper));

        return Optional.empty();
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

    static void reselectFont(int index) {
        selectFont((index >= 0 && index < AVAILABLE_FONTS.size()) ? AVAILABLE_FONTS.get(index) : null);
    }

    static void reselectFont() {
        selectFont(SELECTED_FONT);
    }

    private static @NotNull Path getFontsDir() {
        Path fontsDir = BigSignWriterConfig.getConfigDir().resolve("fonts");
        try {
            Files.createDirectories(fontsDir);
        } catch (IOException e) {
            LOGGER.error("Failed to create fonts directory: {}", fontsDir, e);
        }
        return fontsDir;
    }

    public static void reloadFonts() {
        int selectedFontIndex = AVAILABLE_FONTS.indexOf(SELECTED_FONT);
        List<FontInfo> builtInFonts = copyBuiltInFonts();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path fontsDir = getFontsDir();
        File[] jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            LOGGER.error("Failed to load or create any font files. Mod behavior from here is undefined. Please report!");
            return;
        }

        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        AVAILABLE_FONTS.clear();
        AVAILABLE_FONTS.addAll(builtInFonts);
        AVAILABLE_FONTS.addAll(Arrays.stream(jsonFiles).map(file -> {
            String fileName = file.getName();
            FontInfo fontInfo = getFont(gson, file.toPath()).load().createInfo(fileName);
            if (fileName.equals("default.json")) DEFAULT_FONT = fontInfo;
            return fontInfo;
        }).toList());
        AVAILABLE_FONTS.sort((a, b) -> a.name().compareToIgnoreCase(b.name()));
        reselectFont(selectedFontIndex);
        LOGGER.info("Fonts loaded!");
    }

    public static void saveFontToFile(FontInfo fontInfo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            Files.createDirectories(configFonts);

            String path = fontInfo.name() + "_copy";
            Path target = configFonts.resolve(path + ".json");
            int i = 1;
            while (Files.exists(target)) {
                target = configFonts.resolve(path + "_" + i++ + ".json");
            }

            getFont(gson, target).save(fontInfo.fontFile);
        } catch (Exception e) {
            LOGGER.error("Error saving font", e);
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
                FontSupplier fontSupplier = entry.getValue();
                FontFile fontFile = fontSupplier.get();
                FontInfo fontInfo = fontFile.createInfo("Built-in");

                String path = entry.getKey();
                if (path.equals("default")) DEFAULT_FONT = fontInfo;
                Path target = configFonts.resolve(path + ".json");
                if (Files.notExists(target)) return fontInfo;

                BigSignWriterConfig.ConfigInterface<FontFile> file = getFont(gson, target);
                FontFile existingFont = file.load();

                Map<Character, Set<FontSupplier.PatchCharacter>> patches = fontSupplier.patches();
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
                        for (FontSupplier.PatchCharacter patch : patches.get(character)) {
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

                canRemove = canRemove
                        && existingFont.characters.size() == fontFile.characters.size()
                        && existingFont.characters.keySet().equals(fontFile.characters.keySet());
                if (canRemove) {
                    File targetFile = target.toFile();
                    if (targetFile.delete()) {
                        LOGGER.info("Removed copy of built-in font '{}'", targetFile.getName());
                        return fontInfo;
                    } else LOGGER.error("Failed to remove copy of built-in font '{}'", targetFile.getName());
                }

                boolean needsSaved;
                if (fontFile.credits != null && !fontFile.credits.equals(existingFont.credits)) {
                    existingFont.credits = fontFile.credits;
                    needsSaved = true;
                } else needsSaved = !changed.isEmpty() || !patched.isEmpty();

                if (needsSaved) {
                    file.save(existingFont);

                    if (!changed.isEmpty())
                        LOGGER.info("Merged new characters from built-in font '{}': {}", fontFile.name, changed);
                    if (!patched.isEmpty())
                        LOGGER.info("Patched outdated characters from built-in font '{}': {}", fontFile.name, patched);
                }

                return null;
            }).filter(Objects::nonNull).toList();
        } catch (Exception e) {
            LOGGER.error("Error copying built-in fonts", e);
            return List.of();
        }
    }
}
