//? if forge {
/*package dev.chililisoup.bigsignwriter.loaders.forge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.compat.YaclIntegration;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(BigSignWriter.MOD_ID)
public class ForgeEntrypoint {
    public ForgeEntrypoint() {
        BigSignWriter.initialize();
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parentScreen) -> YaclIntegration.generateScreen(parentScreen))
        );
    }
}
*///?}