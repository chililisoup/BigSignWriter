package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
//? if fabric
import net.fabricmc.loader.api.FabricLoader;
//? if neoforge
//import net.neoforged.fml.loading.FMLPaths;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BigSignWriterConfig {
    public static MainConfig MAIN_CONFIG;

    public static void init() {}

    static {
        reloadConfig();
    }

    public static class MainConfig {
        public int buttonsX = 0;
        public int buttonsY = 120;
        public double buttonsAlignmentX = 0.5;
        public double buttonsAlignmentY = 0.25;
        public boolean showReloadButton = false;
        public String defaultCharacterSeparator = " ";

        public MainConfig() {}
    }

    static Path getConfigDir() {
        //? if fabric {
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BigSignWriter.MOD_ID);
        //?} else
        //Path configDir = FMLPaths.CONFIGDIR.get().resolve(BigSignWriter.MOD_ID);
        
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            BigSignWriter.LOGGER.error("Failed to create config directory: {}", configDir, e);
        }
        return configDir;
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
        BigSignWriter.reselectFont();

        BigSignWriter.LOGGER.info("Config saved!");
    }

    public static void reloadConfig() {
        ConfigInterface<MainConfig> mainConfig = getConfig();

        MAIN_CONFIG = mainConfig.load();
        mainConfig.save(MAIN_CONFIG);

        BigSignWriter.LOGGER.debug("Config loaded!");
    }

    record ConfigInterface<T>(Gson gson, TypeToken<T> typeToken, Path path, T defaultConfig) {
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
