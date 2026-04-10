//? if neoforge {
/*package dev.chililisoup.bigsignwriter.loaders.neoforge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.config.BigSignWriterConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.nio.file.Path;

@Mod(BigSignWriter.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint(ModContainer container) {
        String version = container.getModInfo().getOwningFile().versionString();
        Path configDir = FMLPaths.CONFIGDIR.get().resolve(BigSignWriter.MOD_ID);
        BigSignWriter.initialize(version, configDir);

        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (modContainer, parentScreen) -> new BigSignWriterConfigScreen(parentScreen)
        );
    }
}
*///?}