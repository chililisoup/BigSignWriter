package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

//? if fabric
import net.fabricmc.loader.api.FabricLoader;
//? if neoforge
//import net.neoforged.fml.loading.FMLPaths;

public class BigSignWriterConfig {
    public static final MainConfig MAIN_CONFIG = new MainConfig();

    public static void init() {}

    static {
        reloadConfig();
    }

    public static class PersistentConfig {
        public int buttonsX = 0;
        public int buttonsY = 120;
        public double buttonsAlignmentX = 0.5;
        public double buttonsAlignmentY = 0.25;
        public boolean fontSelectorCoversDoneButton = true;
        public boolean showReloadButton = false;
        public HashSet<String> hiddenFonts = new HashSet<>(List.of(
                "builtin/monospace"
        ));

        public PersistentConfig copyFrom(PersistentConfig other) {
            for (Field field : PersistentConfig.class.getDeclaredFields()) {
                try {
                    if (field.getType() == HashSet.class) continue;
                    field.set(this, field.get(other));
                } catch (IllegalAccessException ignored) {}
            }

            this.hiddenFonts = new HashSet<>(other.hiddenFonts);

            return this;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof PersistentConfig)) return false;
            for (Field field : PersistentConfig.class.getDeclaredFields()) {
                try {
                    if (!field.get(this).equals(field.get(other)))
                        return false;
                } catch (IllegalAccessException ignored) {}
            }
            return true;
        }
    }

    public static class MainConfig extends PersistentConfig {
        public boolean characterSeparatorOverrideEnabled = false;
        public String characterSeparatorOverride = "";

        public static MainConfig of(MainConfig other) {
            return new MainConfig().copyFrom(other);
        }

        public MainConfig createCopy() {
            return of(this);
        }

        public MainConfig copyFrom(MainConfig other) {
            super.copyFrom(other);
            for (Field field : MainConfig.class.getDeclaredFields()) {
                try {
                    field.set(this, field.get(other));
                } catch (IllegalAccessException ignored) {}
            }
            return this;
        }

        @Override
        public boolean equals(Object other) {
            if (!super.equals(other)) return false;
            for (Field field : MainConfig.class.getDeclaredFields()) {
                try {
                    if (!field.get(this).equals(field.get(other)))
                        return false;
                } catch (IllegalAccessException ignored) {}
            }
            return true;
        }
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

    private static ConfigInterface<PersistentConfig> getConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path configDir = getConfigDir();
        return new ConfigInterface<>(
                gson,
                TypeToken.get(PersistentConfig.class),
                configDir.resolve("config.json"),
                new PersistentConfig()
        );
    }

    public static void saveConfig() {
        ConfigInterface<PersistentConfig> persistentConfig = getConfig();

        persistentConfig.save(new PersistentConfig().copyFrom(MAIN_CONFIG));
        BigSignWriter.reselectFont();

        BigSignWriter.LOGGER.info("Config saved!");
    }

    public static void reloadConfig() {
        ConfigInterface<PersistentConfig> persistentConfig = getConfig();

        MAIN_CONFIG.copyFrom(persistentConfig.load());
        persistentConfig.save(new PersistentConfig().copyFrom(MAIN_CONFIG));

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
