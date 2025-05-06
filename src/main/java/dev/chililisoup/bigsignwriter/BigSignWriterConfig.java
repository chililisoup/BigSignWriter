package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


public class BigSignWriterConfig {
    public static FontFile FONT;
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
            System.err.println("Failed to create config directory: " + configDir);
            e.printStackTrace();
        }
        return configDir;
    }


    private static @NotNull Path getFontsDir() {
        Path fontsDir = getConfigDir().resolve("fonts");
        try {
            Files.createDirectories(fontsDir);
        } catch (IOException e) {
            System.err.println("Failed to create fonts directory: " + fontsDir);
            e.printStackTrace();
        }
        return fontsDir;
    }

    public static void reloadFonts() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ConfigInterface<FontFile> charConfig = getMapConfigInterface(gson);
        FONT = charConfig.load();
        BigSignWriter.LOGGER.info("Fonts loaded!");
    }

    private static void copyBuiltInFonts() {
        Path configFonts = getFontsDir();
        try {
            Files.createDirectories(configFonts);
            String resourcePath = "/assets/bigsignwriter/fonts/";
            List<String> builtInFonts = List.of("default.json", "sharp.json", "retro.json");
            for (String fileName : builtInFonts) {
                Path target = configFonts.resolve(fileName);
                if (Files.notExists(target)) {
                    try (InputStream in = BigSignWriterConfig.class.getResourceAsStream(resourcePath + fileName)) {
                        if (in != null) {
                            Files.copy(in, target);
                            BigSignWriter.LOGGER.info("Copied built-in font: {}", fileName);
                        }
                    }
                }
            }
        } catch (IOException e) {
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

    public static int selectedFont = 0;

    public static void getNextFont() {
        File[] jsonFiles = getFontsDir().toFile().listFiles((d, n) -> n.endsWith(".json"));
        assert jsonFiles != null;
        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        int len = jsonFiles.length;
        if (len > 0) {
            selectedFont = (selectedFont + 1) % len;
        }
        BigSignWriter.LOGGER.info("Switched to font index: {}", selectedFont);
    }


    private static @NotNull ConfigInterface<FontFile> getMapConfigInterface(Gson gson) {
        Path fontsDir = getFontsDir();
        File[] jsonFiles = fontsDir.toFile().listFiles((dir, name) -> name.endsWith(".json"));
        assert jsonFiles != null;
        Arrays.sort(jsonFiles, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        File selectedFile = jsonFiles[selectedFont];
        return new ConfigInterface<>(
                gson,
                new TypeToken<>() {},
                selectedFile.toPath(),
                new FontFile()
        );
    }

    static {
        copyBuiltInFonts();
        reloadConfig();
        reloadFonts();
    }

    private record ConfigInterface<T>(Gson gson, TypeToken<T> typeToken, Path path, T defaultConfig) {
        public T load() {
            try (FileReader fileReader = new FileReader(path.toFile())) {
                JsonReader jsonReader = new JsonReader(fileReader);
                return gson.fromJson(jsonReader, typeToken);
            } catch (FileNotFoundException e) {
                this.save(defaultConfig);
                return defaultConfig;
            } catch (Exception e) {
                System.err.println("Failed to load config: " + path.getFileName());
                e.printStackTrace();
                return defaultConfig;
            }
        }

        public void save(T config) {
            try (FileWriter fileWriter = new FileWriter(new File(path.toUri()))) {
                gson.toJson(config, fileWriter);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
