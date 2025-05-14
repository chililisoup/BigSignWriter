//? if forge {
/*package dev.chililisoup.bigsignwriter.loaders.forge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.compat.YaclIntegration;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(BigSignWriter.MOD_ID)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        BigSignWriter.initialize();

        if (ModList.get().isLoaded("yet_another_config_lib_v3")) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (minecraft, parentScreen) -> YaclIntegration.generateScreen(parentScreen)
                    )
            );
        }
    }
}
*///?}