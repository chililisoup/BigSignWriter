package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.font.supplier.*;

import java.util.Map;

import static java.util.Map.entry;

public class BuiltInFonts {
    public static Map<String, FontSupplier> get() {
        return Map.ofEntries(
                entry("default", new DefaultFont()),
                entry("sharp", new SharpFont()),
                entry("retro", new RetroFont()),
                entry("monospace", new MonospaceFont()) // Makes heavy use of default font fallback
        );
    }
}
