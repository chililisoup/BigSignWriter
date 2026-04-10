//? if fabric {
package dev.chililisoup.bigsignwriter.loaders.fabric;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class FabricEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        String version = FabricLoader.getInstance().getModContainer(BigSignWriter.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BigSignWriter.MOD_ID);
        BigSignWriter.initialize(version, configDir);
    }
}
//?}