//? if neoforge {
/*package dev.chililisoup.bigsignwriter.loaders.neoforge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.config.BigSignWriterConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.nio.file.Path;

//? if >= 1.21.4 {
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.resources.VanillaClientListeners;
//?} else {
/^import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
^///?}

@Mod(BigSignWriter.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint(IEventBus modBus, ModContainer container) {
        String version = container.getModInfo().getOwningFile().versionString();
        Path configDir = FMLPaths.CONFIGDIR.get().resolve(BigSignWriter.MOD_ID);
        BigSignWriter.initialize(version, configDir);

        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (modContainer, parentScreen) -> new BigSignWriterConfigScreen(parentScreen)
        );

        modBus.addListener(NeoforgeEntrypoint::registerLoaders);
    }

    public static void registerLoaders(
            //? if >= 1.21.4 {
            AddClientReloadListenersEvent event
            //?} else
            //RegisterClientReloadListenersEvent event
    ) {
        //? if >= 1.21.4 {
        Identifier bigFontManager = BigSignWriter.id("big_font_manager");
        event.addListener(bigFontManager, BigSignWriter.getBigFontManager());
        event.addDependency(VanillaClientListeners.FONTS, bigFontManager);
        //?} else {
        /^event.registerReloadListener(BigSignWriter.getBigFontManager());
        ^///?}
    }
}
*///?}