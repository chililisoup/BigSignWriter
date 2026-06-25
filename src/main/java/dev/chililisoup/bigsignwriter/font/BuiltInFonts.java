package dev.chililisoup.bigsignwriter.font;

import dev.chililisoup.bigsignwriter.font.supplier.*;

import java.util.Map;

import static java.util.Map.entry;

public class BuiltInFonts {
    public static Map<String, FontSupplier> get() {
        return Map.ofEntries(
                entry("default", DefaultFont::get),
                entry("default_mono", DefaultMonoFont::get),
                entry("sharp", SharpFont::get),
                entry("retro", RetroFont::get),
                entry("thin", ThinFont::get),
                entry("small", SmallFont::get),
                //? if >= 1.21.4 {
                entry("smooth_small", SmoothSmallFont::get),
                entry("happy_ghast", HappyGhastFont::get),
                entry("endermite", EndermiteFont::get),
                entry("chillager", ChillagerFont::get),
                entry("plains", PlainsFont::get),
                entry("queats_pro", QueatsProFont::get),
                entry("queats_pro_monospace", QueatsProMonospaceFont::get),
                entry("queats_lazy_sunday", QueatsLazySundayFont::get),
                entry("queatlets", QueatletsFont::get),
                entry("queatlets_monospace", QueatletsMonospaceFont::get),
                entry("queatlets_old_style", QueatletsOldStyleFont::get),
                entry("queatlets_old_style_monospace", QueatletsOldStyleMonospaceFont::get),
                entry("minecraft_striped", MinecraftStripedFont::get),
                entry("minecraft_small", MinecraftSmallFont::get),
                //?}
                entry("minecraft", MinecraftFont::get),
                entry("outlined", OutlinedFont::get),
                entry("outlined_short", OutlinedShortFont::get),
                entry("shulkerware", ShulkerwareFont::get)
        );
    }

    @FunctionalInterface
    public interface FontSupplier {
        FontFile get();
    }
}
