package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class BigSignWriterConfig {
    public static FontFile SELECTED_FONT;
    private static int SELECTED_FONT_INDEX;
    public static List<FontFile> AVAILABLE_FONTS;
    public static MainConfig MAIN_CONFIG;

    public static class MainConfig {
        public int toggleButtonX = 0;
        public int toggleButtonY = 120;
        public String characterSeparator = " ";
        MainConfig() {}
    }

    private static Path getConfigDir() {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve("bigsignwriter");
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path fontsDir = getFontsDir();
        File[] jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            BigSignWriter.LOGGER.error("No fonts found. Recreating built-in fonts.");
            copyBuiltInFonts();
            jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));
            if (jsonFiles == null || jsonFiles.length == 0) {
                BigSignWriter.LOGGER.error("Built-in font recreation failed to produce new font files. Mod behavior from here is undefined. Please report!");
                return;
            }
        }

        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));

        AVAILABLE_FONTS = Arrays.stream(jsonFiles).map(file -> getFont(gson, file.toPath()).load()).toList();

        SELECTED_FONT_INDEX = SELECTED_FONT_INDEX >= AVAILABLE_FONTS.size() ? 0 : SELECTED_FONT_INDEX;
        SELECTED_FONT = AVAILABLE_FONTS.get(SELECTED_FONT_INDEX);

        BigSignWriter.LOGGER.info("Fonts loaded!");
    }

    private static ConfigInterface<FontFile> getFont(Gson gson, Path path) {
        return new ConfigInterface<>(
                gson,
                new TypeToken<>() {},
                path,
                new FontFile()
        );
    }

    private static void copyBuiltInFonts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configFonts = getFontsDir();

        try {
            Files.createDirectories(configFonts);

            Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(BigSignWriter.MOD_ID);
            if (container.isEmpty()) throw new Exception("Failed to get mod container.");

            Optional<Path> path = container.get().findPath("assets/" + BigSignWriter.MOD_ID + "/fonts");
            if (path.isEmpty()) throw new Exception("Failed to get built-in fonts folder.");
            
            File[] builtInFonts = path.get().toFile().listFiles();
            if (builtInFonts == null) throw new Exception("Failed to get files from the built-in fonts folder.");
            
            for (File file : builtInFonts) {
                Path target = configFonts.resolve(file.getName());
                if (Files.notExists(target)) {
                    Files.copy(new FileInputStream(file), target);
                    BigSignWriter.LOGGER.info("Copied built-in font: {}", file.getName());
                    continue;
                }

                ConfigInterface<FontFile> existing = getFont(gson, target);
                FontFile existingFont = existing.load();
                FontFile builtIn = getFont(gson, file.toPath()).load();

                ArrayList<Character> changed = new ArrayList<>();
                for (char character : builtIn.characters.keySet()) {
                    if (existingFont.characters.containsKey(character)) continue;

                    existingFont.characters.put(character, builtIn.characters.get(character));
                    changed.add(character);
                }

                if (!changed.isEmpty()) {
                    existing.save(existingFont);
                    BigSignWriter.LOGGER.info("Merged new characters from built-in font '{}': {}", builtIn.name, changed);
                }
            }
        } catch (Exception e) {
            BigSignWriter.LOGGER.error("Error copying built-in fonts", e);
        }
    }

    public static void reloadConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configDir = getConfigDir();
        ConfigInterface<MainConfig> mainConfig = new ConfigInterface<>(
                gson,
                TypeToken.get(MainConfig.class),
                configDir.resolve("config.json"),
                new MainConfig()
        );
        MAIN_CONFIG = mainConfig.load();
        mainConfig.save(MAIN_CONFIG);

        BigSignWriter.LOGGER.info("Configs loaded!");
    }

    public static void getNextFont() {
        SELECTED_FONT_INDEX = (SELECTED_FONT_INDEX + 1) % AVAILABLE_FONTS.size();
        SELECTED_FONT = AVAILABLE_FONTS.get(SELECTED_FONT_INDEX);

        BigSignWriter.LOGGER.debug("Switched to font {} at index {}", SELECTED_FONT.name, SELECTED_FONT_INDEX);
    }

    static {
        copyBuiltInFonts();
        reloadConfig();
        reloadFonts();
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
