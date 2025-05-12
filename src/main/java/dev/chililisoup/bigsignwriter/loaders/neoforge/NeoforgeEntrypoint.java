//? if neoforge {
/*package dev.chililisoup.bigsignwriter.loaders.neoforge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.compat.YaclIntegration;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BigSignWriter.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint(ModContainer container) {
        BigSignWriter.initialize();

        if (ModList.get().isLoaded("yet_another_config_lib_v3")) {
            container.registerExtensionPoint(
                    IConfigScreenFactory.class,
                    (modContainer, parentScreen) -> YaclIntegration.generateScreen(parentScreen)
            );
        }
    }
}
*///?}