package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dev.chililisoup.bigsignwriter.font.*;
//? if fabric
import dev.chililisoup.bigsignwriter.font.supplier.FontSupplier;
import net.fabricmc.loader.api.FabricLoader;
//? if neoforge
/*import net.neoforged.fml.loading.FMLPaths;*/
//? if forge
/*import net.minecraftforge.fml.loading.FMLPaths;*/
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BigSignWriterConfig {
    public static FontFile SELECTED_FONT;
    public static String CHARACTER_SEPARATOR;
    public static List<FontFile> AVAILABLE_FONTS;
    public static FontFile DEFAULT_FONT;
    public static MainConfig MAIN_CONFIG;
    private static int SELECTED_FONT_INDEX;

    public static void noop() {}

    static {
        reloadConfig();
        reloadFonts();
    }

    private static Path getConfigDir() {
        //? if fabric {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BigSignWriter.MOD_ID);
        //?} else
        /*Path configDir = FMLPaths.CONFIGDIR.get().resolve(BigSignWriter.MOD_ID);*/
        
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            BigSignWriter.LOGGER.error("Failed to create config directory: {}", configDir, e);
        }
        return configDir;
    }

    private static @NotNull Path getFontsDir() {
        Path fontsDir = getConfigDir().resolve("fonts");
        try {
            Files.createDirectories(fontsDir);
        } catch (IOException e) {
            BigSignWriter.LOGGER.error("Failed to create fonts directory: {}", fontsDir, e);
        }
        return fontsDir;
    }

    public static void reloadFonts() {
        copyBuiltInFonts();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path fontsDir = getFontsDir();
        File[] jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            BigSignWriter.LOGGER.error("Failed to load or create any font files. Mod behavior from here is undefined. Please report!");
            return;
        }

        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        AVAILABLE_FONTS = Arrays.stream(jsonFiles).map(file -> getFont(gson, file.toPath()).load()).toList();
        selectFont(SELECTED_FONT_INDEX >= AVAILABLE_FONTS.size() ? 0 : SELECTED_FONT_INDEX);
        BigSignWriter.LOGGER.info("Fonts loaded!");
    }

    private static ConfigInterface<FontFile> getFont(Gson gson, Path path) {
        return new ConfigInterface<>(
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
                FontFile fontDefaults = font.get();
                Path target = configFonts.resolve(path + ".json");
                ConfigInterface<FontFile> file = getFont(gson, target);

                if (Files.notExists(target)) {
                    file.save(fontDefaults);
                    BigSignWriter.LOGGER.info("Copied built-in font: {}", fontDefaults.name);
                    if (fontDefaults.name.equals("Default"))
                        DEFAULT_FONT = fontDefaults;
                    return;
                }

                FontFile existingFont = file.load();
                Map<Character, Set<FontSupplier.PatchCharacter>> patches = font.patches();
                ArrayList<Character> changed = new ArrayList<>();
                ArrayList<Character> patched = new ArrayList<>();
                for (char character : fontDefaults.characters.keySet()) {
                    if (existingFont.characters.containsKey(character)) {
                        if (!patches.containsKey(character)) continue;

                        String existing = String.join("\n", existingFont.characters.get(character));
                        for (FontSupplier.PatchCharacter patch : patches.get(character)) {
                            if (String.join("\n", patch.lines()).matches(existing)) {
                                existingFont.characters.put(character, fontDefaults.characters.get(character));
                                patched.add(character);
                                break;
                            }
                        }

                        continue;
                    }

                    existingFont.characters.put(character, fontDefaults.characters.get(character));
                    changed.add(character);
                }

                if (!changed.isEmpty() || !patched.isEmpty()) {
                    file.save(existingFont);

                    if (!changed.isEmpty())
                        BigSignWriter.LOGGER.info("Merged new characters from built-in font '{}': {}", fontDefaults.name, changed);
                    if (!patched.isEmpty())
                        BigSignWriter.LOGGER.info("Patched outdated characters from built-in font '{}': {}", fontDefaults.name, patched);
                }

                if (existingFont.name.equals("Default"))
                    DEFAULT_FONT = existingFont;
            });
        } catch (Exception e) {
            BigSignWriter.LOGGER.error("Error copying built-in fonts", e);
        }
    }

    private static ConfigInterface<MainConfig> getConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configDir = getConfigDir();
        return new ConfigInterface<>(
                gson,
                TypeToken.get(MainConfig.class),
                configDir.resolve("config.json"),
                new MainConfig()
        );
    }

    public static void saveConfig() {
        ConfigInterface<MainConfig> mainConfig = getConfig();

        mainConfig.save(MAIN_CONFIG);
        selectFont(SELECTED_FONT_INDEX);

        BigSignWriter.LOGGER.info("Config saved!");
    }

    public static void reloadConfig() {
        ConfigInterface<MainConfig> mainConfig = getConfig();

        MAIN_CONFIG = mainConfig.load();
        mainConfig.save(MAIN_CONFIG);

        BigSignWriter.LOGGER.debug("Config loaded!");
    }

    private static void selectFont(int index) {
        SELECTED_FONT_INDEX = index % AVAILABLE_FONTS.size();
        SELECTED_FONT = AVAILABLE_FONTS.get(SELECTED_FONT_INDEX);
        CHARACTER_SEPARATOR = SELECTED_FONT.characterSeparator == null ?
                MAIN_CONFIG.defaultCharacterSeparator :
                SELECTED_FONT.characterSeparator;
    }

    public static void getNextFont() {
        selectFont(SELECTED_FONT_INDEX + 1);
        BigSignWriter.LOGGER.debug("Switched to font {} at index {}", SELECTED_FONT.name, SELECTED_FONT_INDEX);
    }

    public static class MainConfig {
        public int buttonsX = 0;
        public int buttonsY = 120;
        public double buttonsAlignmentX = 0.5;
        public double buttonsAlignmentY = 0.25;
        public String defaultCharacterSeparator = " ";

        public MainConfig() {
        }
    }

    private record ConfigInterface<T>(Gson gson, TypeToken<T> typeToken, Path path, T defaultConfig) {
        public T load() {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
                JsonReader jsonReader = new JsonReader(reader);
                return gson.fromJson(jsonReader, typeToken);
            } catch (FileNotFoundException e) {
                this.save(defaultConfig);
                return defaultConfig;
            } catch (Exception e) {
                BigSignWriter.LOGGER.error("Failed to load config: {}", path.getFileName(), e);
                return defaultConfig;
            }
        }

        public void save(T config) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(path.toFile()), StandardCharsets.UTF_8)) {
                gson.toJson(config, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
