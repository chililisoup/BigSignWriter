package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class BigSignWriterConfig {
    public static Map<Character, String[]> CHARACTERS;
    public static MainConfig MAIN_CONFIG;

    public static class MainConfig {
        public int toggleButtonX = 0;
        public int toggleButtonY = 120;
        public String characterSeparator = " ";
        MainConfig() {}
    }

    public static void reload() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configDir = FabricLoader.getInstance().getConfigDir();

        ConfigInterface<Map<Character, String[]>> charConfig = new ConfigInterface<>(
                gson,
                new TypeToken<>() {},
                configDir.resolve("bigsignwriter-characters.json"),
                DefaultCharacterConfig.DEFAULT_CHARACTERS
        );
        CHARACTERS = charConfig.load();
        DefaultCharacterConfig.DEFAULT_CHARACTERS.forEach((chr, bigChar) -> {
            if (!CHARACTERS.containsKey(chr))
                CHARACTERS.put(chr, bigChar);
        });
        charConfig.save(CHARACTERS);

        ConfigInterface<MainConfig> mainConfig = new ConfigInterface<>(
                gson,
                TypeToken.get(MainConfig.class),
                configDir.resolve("bigsignwriter-config.json"),
                new MainConfig()
        );
        MAIN_CONFIG = mainConfig.load();
        mainConfig.save(MAIN_CONFIG);

        BigSignWriter.LOGGER.info("Configs loaded!");
    }

    static {
        reload();
    }

    private record ConfigInterface<T>(Gson gson, TypeToken<T> typeToken, Path path, T defaultConfig) {
        public T load() {
            try {
                JsonReader jsonReader = new JsonReader(new FileReader(path.toFile()));
                return gson.fromJson(jsonReader, typeToken);
            } catch (FileNotFoundException e) {
                this.save(defaultConfig);
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
