package dev.chililisoup.bigsignwriter.gui.config;

import dev.chililisoup.bigsignwriter.BigSignWriter;
import dev.chililisoup.bigsignwriter.font.FontInfo;
import dev.chililisoup.bigsignwriter.gui.TickBox;
import dev.chililisoup.bigsignwriter.util.GraphicsHelper;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FontsTab extends AbstractFontsTab {
    public FontsTab(BigSignWriterConfigScreen screen) {
        super(screen, Component.translatable("bigsignwriter.config.fonts"));
    }

    @Override
    protected Stream<FontInfo> getShownFonts() {
        return BigSignWriter.availableFonts().stream().filter(FontInfo::hasCharacters);
    }

    @Override
    protected FontsSidePanel buildSidePanel() {
        return new FontsSidePanel();
    }

    protected final class FontsSidePanel extends AbstractFontsSidePanel {
        private boolean showInheritedCharacters = true;

        private final TickBox inheritedCharactersToggle = new TickBox(
                true,
                value -> {
                    this.showInheritedCharacters = value;
                    FontsTab.this.redoLayout();
                },
                Component.translatable("bigsignwriter.config.fonts.showInheritedCharacters")
        );

        @Override
        protected void addExtraWidgets(Consumer<AbstractWidget> widgetConsumer) {
            super.addExtraWidgets(widgetConsumer);
            widgetConsumer.accept(this.inheritedCharactersToggle);
        }

        @Override
        protected void updateButtonVisibility(boolean working, @Nullable FontInfo fontInfo) {
            super.updateButtonVisibility(working, fontInfo);
            this.inheritedCharactersToggle.visible = working
                    && fontInfo != null
                    && fontInfo.hasExplicitParent();
        }

        @Override
        protected void addInfoLines(FontInfo fontInfo, Consumer<Component> lines) {
            Optional.ofNullable(fontInfo.credits()).ifPresent(
                    credits -> lines.accept(infoLine("bigsignwriter.font.info.credits", credits))
            );
            lines.accept(infoLine("bigsignwriter.font.info.id", fontInfo.id));
            FontInfo parentFont = fontInfo.parentFont();
            if (parentFont != null) lines.accept(infoLine(
                    fontInfo.parentIsImplicit() ?
                            "bigsignwriter.font.info.parentFont.implicit" :
                            "bigsignwriter.font.info.parentFont",
                    parentFont.name()
            ));
            lines.accept(fontInfo.hasExplicitParent() ?
                    infoLine(
                            "bigsignwriter.font.info.characterCount.cumulative",
                            fontInfo.cumulativeCharacters().size(),
                            fontInfo.characters().size()
                    ) :
                    infoLine("bigsignwriter.font.info.characterCount", fontInfo.characters().size())
            );
            if (fontInfo.isWorking()) {
                String cumulativeWidthInfo = fontInfo.cumulativeWidthInfo();
                lines.accept(cumulativeWidthInfo == null ?
                        infoLine("bigsignwriter.font.info.width", fontInfo.widthInfo()) :
                        infoLine(
                                "bigsignwriter.font.info.width.cumulative",
                                cumulativeWidthInfo,
                                fontInfo.widthInfo()
                        )
                );
                lines.accept(infoLine("bigsignwriter.font.info.height", fontInfo.height()));
            }
        }

        @Override
        protected @NotNull List<Component[]> getWrappedFontPreview(FontInfo fontInfo) {
            Set<Character> charSet = this.showInheritedCharacters ?
                    fontInfo.cumulativeCharacters() :
                    fontInfo.characters().keySet();
            return GraphicsHelper.getWrappedFontPreview(
                    fontInfo,
                    String.join("", charSet.stream().map(String::valueOf).toArray(String[]::new)),
                    this.width,
                    PREVIEW_LINE_HEIGHT
            );
        }

        @Override
        protected boolean copyButtonOccupiesFullRow() {
            return this.inheritedCharactersToggle.visible;
        }

        @Override
        protected void arrangeButtons(int y) {
            super.arrangeButtons(y);
            this.inheritedCharactersToggle.setPosition(this.getX() + 44, y);
            this.inheritedCharactersToggle.setWidth(this.getWidth() - 44);
        }
    }
}
