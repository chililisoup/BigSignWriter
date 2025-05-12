//? if fabric {
package dev.chililisoup.bigsignwriter.loaders.fabric;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import net.fabricmc.api.ClientModInitializer;

public class FabricEntrypoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BigSignWriter.initialize();
    }
}
//?}