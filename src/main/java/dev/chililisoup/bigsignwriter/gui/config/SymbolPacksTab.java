package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class SymbolPacksTab extends AbstractFontsTab {
    public SymbolPacksTab(BigSignWriterConfigScreen screen) {
        super(screen, Component.translatable("bigsignwriter.config.symbol_packs"));
    }

    @Override
    protected Stream<FontInfo> getShownFonts() {
        return BigSignWriter.availableFonts().stream().filter(FontInfo::hasExplicitSymbols);
    }

    @Override
    protected @Nullable Component getErrorMessage(FontInfo fontInfo) {
        return fontInfo.symbolError();
    }

    @Override
    protected SymbolPacksSidePanel buildSidePanel() {
        return new SymbolPacksSidePanel();
    }

    protected final class SymbolPacksSidePanel extends AbstractFontsSidePanel {
        @Override
        protected void addInfoLines(FontInfo fontInfo, Consumer<Component> lines) {
            Optional.ofNullable(fontInfo.credits()).ifPresent(
                    credits -> lines.accept(infoLine("bigsignwriter.font.info.credits", credits))
            );
            lines.accept(infoLine("bigsignwriter.font.info.id", fontInfo.id));
            lines.accept(infoLine("bigsignwriter.font.info.symbolCount", fontInfo.explicitSymbols().size()));
            if (fontInfo.areSymbolsWorking()) {
                lines.accept(infoLine("bigsignwriter.font.info.width", fontInfo.symbolWidthInfo()));
                lines.accept(infoLine("bigsignwriter.font.info.height", fontInfo.symbolHeightInfo()));
            }
        }

        @Override
        protected @NotNull List<Component[]> getWrappedFontPreview(FontInfo fontInfo) {
            return GraphicsHelper.getWrappedSymbolsPreview(
                    fontInfo,
                    this.width,
                    PREVIEW_LINE_HEIGHT
            );
        }
    }
}
