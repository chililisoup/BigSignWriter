//? if fabric {
package dev.chililisoup.bigsignwriter.loaders.fabric;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.packs.PackType;

import java.nio.file.Path;

//? if >= 1.21.9 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.resources.Identifier;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
*///?}

public class FabricEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        String version = FabricLoader.getInstance().getModContainer(BigSignWriter.MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
        Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BigSignWriter.MOD_ID);
        BigSignWriter.initialize(version, configDir);

        //? if >= 1.21.9 {
        Identifier bigFontManager = BigSignWriter.id("big_font_manager");
        ResourceLoader resourceLoader = ResourceLoader.get(PackType.CLIENT_RESOURCES);
        //~ if >= 26.1 'registerReloader' -> 'registerReloadListener'
        resourceLoader.registerReloadListener(bigFontManager, BigSignWriter.getBigFontManager());
        //~ if >= 26.1 'addReloaderOrdering' -> 'addListenerOrdering'
        resourceLoader.addListenerOrdering(ResourceReloaderKeys.Client.FONTS, bigFontManager);
        //?} else {
        /*ResourceManagerHelper resourceLoader = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        resourceLoader.registerReloadListener(BigSignWriter.getBigFontManager());
        *///?}
    }
}
//?}