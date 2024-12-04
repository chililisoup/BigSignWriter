package dev.chililisoup.bigsignwriter;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigSignWriter implements ClientModInitializer {
    public static final String MOD_ID = "bigsignwriter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static boolean ENABLED = false;
    public static void toggleEnabled() {
        ENABLED = !ENABLED;
    }

    @Override
    public void onInitializeClient() {
        // Reference config class, to cause it to load during initialization
        // (instead of first time a sign edit gui is opened)
        try {
            Class.forName("dev.chililisoup.bigsignwriter.BigSignWriterConfig");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
