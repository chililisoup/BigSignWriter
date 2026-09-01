package dev.chililisoup.bigsignwriter.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import dev.chililisoup.bigsignwriter.BigSignWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public record ConfigInterface<T>(Gson gson, TypeToken<T> typeToken, Path path, T defaultConfig) {
    public T load() {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(path.toFile()), StandardCharsets.UTF_8)) {
            JsonReader jsonReader = new JsonReader(reader);
            return gson.fromJson(jsonReader, typeToken);
        } catch (FileNotFoundException e) {
            this.save(defaultConfig);
            return defaultConfig;
        } catch (Exception e) {
            BigSignWriter.LOGGER.error(BigSignWriter.LOGGER_PREFIX + "Failed to load config: {}", path.getFileName(), e);
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
