package dev.chililisoup.bigsignwriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.chililisoup.bigsignwriter.font.*;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//? if < 1.21.9 && fabric {
/*import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.resources.Identifier;
*///?}

//? if < 1.21.9 {
/*import net.minecraft.server.packs.resources.ResourceManager;
*///?}

//? if <= 1.21.1 {
/*import net.minecraft.util.profiling.ProfilerFiller;
*///?}

public final class BigFontManager implements
        //? if >= 1.21.9 || neoforge {
        PreparableReloadListener
        //?} else
        //IdentifiableResourceReloadListener
{
    public static final String DEFAULT_FONT_SOURCE = "builtin/default";

    private final ArrayList<FontInfo> availableFonts = new ArrayList<>();
    private final ArrayList<SymbolGroup> availableSymbolGroups = new ArrayList<>();
    private @Nullable FontInfo selectedFont = null;

    public List<FontInfo> availableFonts() {
        return this.availableFonts;
    }

    public List<SymbolGroup> availableSymbolGroups() {
        return this.availableSymbolGroups;
    }

    public @Nullable FontInfo selectedFont() {
        return this.selectedFont;
    }

    public String characterSeparator() {
        return this.selectedFont != null ?
                this.selectedFont.characterSeparator() :
                BigSignWriterConfig.MAIN_CONFIG.characterSeparatorOverride;
    }

    public void selectFont(@Nullable FontInfo fontInfo) {
        this.selectedFont = (fontInfo != null
                && this.availableFonts.contains(fontInfo)
                && fontInfo.isVisible()
        ) ? fontInfo : null;
    }

    private void reselectFont(@Nullable String source) {
        if (source != null) for (FontInfo fontInfo : this.availableFonts) {
            if (fontInfo.source.equals(source)) {
                this.selectFont(fontInfo);
                return;
            }
        }
        this.selectFont(null);
    }

    public void reselectFont() {
        selectFont(this.selectedFont);
    }

    public void forceReload() {
        this.apply(this.prepare());
    }

    @Override
    public @NotNull CompletableFuture<Void> reload(
            //? if >= 1.21.9 {
            @NotNull SharedState currentReload,
            @NotNull Executor taskExecutor,
            @NotNull PreparationBarrier preparationBarrier,
            @NotNull Executor reloadExecutor
            //?} else {
            /*@NotNull PreparableReloadListener.PreparationBarrier preparationBarrier,
            @NotNull ResourceManager manager,
            //? if <= 1.21.1 {
            /^@NotNull ProfilerFiller preparationsProfiler,
            @NotNull ProfilerFiller reloadProfiler,
            ^///?}
            @NotNull Executor taskExecutor,
            @NotNull Executor reloadExecutor
            *///?}
    ) {
        return CompletableFuture.supplyAsync(this::prepare, taskExecutor)
                .thenCompose(preparationBarrier::wait)
                .thenAcceptAsync(this::apply, reloadExecutor);
    }

    private Preparation prepare() {
        String selectedFontSource = this.selectedFont != null ? this.selectedFont.source : null;

        this.availableFonts.clear();
        this.availableSymbolGroups.clear();
        this.selectedFont = null;

        Stream<Map.Entry<String, FontFile>> combinedStream;
        Stream<Map.Entry<String, FontFile>> builtInFontStream = BuiltInFonts.get().entrySet().stream()
                .map(entry -> Map.entry("builtin/" + entry.getKey(), entry.getValue().get()));

        File[] jsonFiles = BigSignWriter.getFontsDir().toFile().listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles != null && jsonFiles.length > 0) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Stream<Map.Entry<String, FontFile>> userFontStream = Arrays.stream(jsonFiles)
                    .map(file -> Map.entry(file.getName(), BigSignWriter.getFontFileInterface(gson, file.toPath()).load()));

            combinedStream = Stream.concat(builtInFontStream, userFontStream);
        } else combinedStream = builtInFontStream;

        return new Preparation(
                selectedFontSource,
                FontInfoExtractor.prepareFonts(combinedStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
        );
    }

    private void apply(Preparation preparation) {
        this.availableFonts.addAll(FontInfoExtractor.extractAll(preparation.preparedFonts));
        this.availableFonts.sort(BigFontManager::compareFonts);
        this.availableSymbolGroups.addAll(SymbolGroup.availableGroups());
        this.reselectFont(preparation.selectedFontSource);
        BigSignWriter.LOGGER.info(BigSignWriter.LOGGER_PREFIX + "Fonts loaded!");
    }

    private static int compareFonts(FontInfo a, FontInfo b) {
        if (a == b) return 0;

        FontInfo aRoot = a.rootAncestorFont();
        if (b == aRoot || b.isDefault()) return 1;
        FontInfo bRoot = b.rootAncestorFont();
        if (a == bRoot || a.isDefault()) return -1;

        if (aRoot != null && aRoot.isDefault()) return -1;
        if (bRoot != null && bRoot.isDefault()) return 1;

        if (aRoot == bRoot) return a.name().compareToIgnoreCase(b.name());

        return (aRoot != null ? aRoot : a).name().compareToIgnoreCase(
                (bRoot != null ? bRoot : b).name()
        );
    }

    private record Preparation(
            @Nullable String selectedFontSource,
            Map<String, FontInfoExtractor.FontInfoExtraction> preparedFonts
    ) {}

    //? if < 1.21.9 && fabric {
    /*private static final Identifier ID = BigSignWriter.id("big_font_manager");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public Collection<Identifier> getFabricDependencies() {
        return List.of(ResourceReloadListenerKeys.FONTS);
    }
    *///?}
}
