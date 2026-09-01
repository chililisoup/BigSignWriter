//? if fabric {
package dev.chililisoup.bigsignwriter.loaders.fabric;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import java.nio.file.Path;

public class FabricEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        String version = FabricLoader.getInstance().getModContainer(BigSignWriter.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BigSignWriter.MOD_ID);
        BigSignWriter.initialize(version, configDir);

        //~ if >= 26.1 'registerReloader' -> 'registerReloadListener' {
        //~ if >= 26.1 'addReloaderOrdering' -> 'addListenerOrdering' {
        ResourceLoader resourceLoader = ResourceLoader.get(PackType.CLIENT_RESOURCES);

        Identifier bigFontResourceProvider = BigSignWriter.id("big_font_resource_provider");
        resourceLoader.registerReloadListener(bigFontResourceProvider, BigSignWriter.getBigFontResourceProvider());

        Identifier bigFontManager = BigSignWriter.id("big_font_manager");
        resourceLoader.registerReloadListener(bigFontManager, BigSignWriter.getBigFontManager());
        resourceLoader.addListenerOrdering(ResourceReloaderKeys.Client.FONTS, bigFontManager);
        resourceLoader.addListenerOrdering(bigFontResourceProvider, bigFontManager);
        //~}
        //~}
    }
}
//?}