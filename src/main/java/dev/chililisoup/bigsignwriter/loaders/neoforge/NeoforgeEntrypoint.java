//? if neoforge {
/*package dev.chililisoup.bigsignwriter.loaders.neoforge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.gui.config.BigSignWriterConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BigSignWriter.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint(ModContainer container) {
        BigSignWriter.initialize();

        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (modContainer, parentScreen) -> new BigSignWriterConfigScreen(parentScreen)
        );
    }
}
*///?}