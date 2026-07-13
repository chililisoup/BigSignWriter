package dev.chililisoup.bigsignwriter.resources;

import dev.chililisoup.bigsignwriter.font.FontFile;
import dev.chililisoup.bigsignwriter.font.FontInfoExtractor;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class BigFontResourceProvider extends SimpleJsonResourceReloadListener<FontFile> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("bigsignwriter");

    public Map<String, FontInfoExtractor.FontInfoExtraction> preparedFonts = Map.of();

    public BigFontResourceProvider() {
        super(FontFile.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(
            @NotNull Map<Identifier, FontFile> preparations,
            @NotNull ResourceManager manager,
            @NotNull ProfilerFiller profiler
    ) {
        this.preparedFonts = FontInfoExtractor.prepareFonts(
                preparations.entrySet().stream()
                        .collect(Collectors.toUnmodifiableMap(
                                entry -> entry.getKey().toString(),
                                Map.Entry::getValue
                        ))
        );
    }
}
