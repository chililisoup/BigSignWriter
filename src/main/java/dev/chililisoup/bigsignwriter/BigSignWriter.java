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
import java.util.regex.Pattern;

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

        CHARACTER_SEPARATOR = BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverrideEnabled ?
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride :
                (SELECTED_FONT != null && SELECTED_FONT.characterSeparator() != null ? SELECTED_FONT.characterSeparator() : " ");
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
        copyBuiltInFonts();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path fontsDir = getFontsDir();
        File[] jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            LOGGER.error("Failed to load or create any font files. Mod behavior from here is undefined. Please report!");
            return;
        }

        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        AVAILABLE_FONTS.clear();
        AVAILABLE_FONTS.addAll(Arrays.stream(jsonFiles).map(file -> {
            FontInfo fontInfo = getFont(gson, file.toPath()).load().createInfo();
            if (file.getName().equals("default.json")) DEFAULT_FONT = fontInfo;
            return fontInfo;
        }).toList());
        reselectFont(selectedFontIndex);
        LOGGER.info("Fonts loaded!");
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

    private static void copyBuiltInFonts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            Files.createDirectories(configFonts);

            BuiltInFonts.get().forEach((path, font) -> {
                FontFile fontFile = font.get();
                Path target = configFonts.resolve(path + ".json");
                BigSignWriterConfig.ConfigInterface<FontFile> file = getFont(gson, target);

                if (Files.notExists(target)) {
                    file.save(fontFile);
                    LOGGER.info("Copied built-in font: {}", fontFile.name);
                    return;
                }

                FontFile existingFont = file.load();
                Map<Character, Set<FontSupplier.PatchCharacter>> patches = font.patches();
                ArrayList<Character> changed = new ArrayList<>();
                ArrayList<Character> patched = new ArrayList<>();
                for (char character : fontFile.characters.keySet()) {
                    if (existingFont.characters.containsKey(character)) {
                        if (!patches.containsKey(character)) continue;

                        String existing = Pattern.quote(String.join("\n", existingFont.characters.get(character)));
                        for (FontSupplier.PatchCharacter patch : patches.get(character)) {
                            if (String.join("\n", patch.lines()).matches(existing)) {
                                existingFont.characters.put(character, fontFile.characters.get(character));
                                patched.add(character);
                                break;
                            }
                        }

                        continue;
                    }

                    existingFont.characters.put(character, fontFile.characters.get(character));
                    changed.add(character);
                }

                if (!changed.isEmpty() || !patched.isEmpty()) {
                    file.save(existingFont);

                    if (!changed.isEmpty())
                        LOGGER.info("Merged new characters from built-in font '{}': {}", fontFile.name, changed);
                    if (!patched.isEmpty())
                        LOGGER.info("Patched outdated characters from built-in font '{}': {}", fontFile.name, patched);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error copying built-in fonts", e);
        }
    }
}
