package dev.chililisoup.bigsignwriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigSignWriter {
    public static final String MOD_ID = "bigsignwriter";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static boolean ENABLED = false;
    public static void toggleEnabled() {
        ENABLED = !ENABLED;
    }

    public static void initialize() {
        // Reference config class, to cause it to load during initialization
        // (instead of first time a sign edit gui is opened)
        BigSignWriterConfig.noop();
    }
}
