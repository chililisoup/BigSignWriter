package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.font.supplier.DefaultFont;
import dev.chililisoup.bigsignwriter.font.supplier.FontSupplier;
import dev.chililisoup.bigsignwriter.font.supplier.RetroFont;
import dev.chililisoup.bigsignwriter.font.supplier.SharpFont;

import java.util.Map;

import static java.util.Map.entry;

public class BuiltInFonts {
    public static Map<String, FontSupplier> get() {
        return Map.ofEntries(
                entry("default", new DefaultFont()),
                entry("sharp", new SharpFont()),
                entry("retro", new RetroFont())
        );
    }
}
