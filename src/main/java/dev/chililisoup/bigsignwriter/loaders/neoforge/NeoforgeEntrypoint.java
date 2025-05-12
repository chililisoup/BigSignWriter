//? if neoforge {
/*package dev.chililisoup.bigsignwriter.loaders.neoforge;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.compat.YaclIntegration;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(BigSignWriter.MOD_ID)
public class NeoforgeEntrypoint {
    public NeoforgeEntrypoint() {
        BigSignWriter.initialize();
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parentScreen) -> YaclIntegration.generateScreen(parentScreen)
        );
    }
}
*///?}