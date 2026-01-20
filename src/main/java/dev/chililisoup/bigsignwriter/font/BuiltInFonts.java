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
                entry("monospace", new MonospaceFont()), // Makes heavy use of default font fallback
                entry("thin", new ThinFont()),
                entry("small", new SmallFont()),
                //? if >= 1.21.6
                entry("smooth_small", new SmoothSmallFont()),
                entry("outlined", new OutlinedFont()),
                entry("outlined_short", new OutlinedShortFont())
        );
    }
}
